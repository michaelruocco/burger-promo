SET GLOBAL log_bin_trust_function_creators = 1;
SET GLOBAL event_scheduler = ON;

DELIMITER $$
CREATE FUNCTION calculate_total_promo_claims(
    promo_id VARCHAR(255)
)
RETURNS INT
BEGIN
    DECLARE count INT;
	DECLARE cache_count INT;

    SELECT COUNT(claim_number) INTO count FROM promo_claim WHERE promo_claim.promo_id = promo_id;
    SELECT claims INTO cache_count FROM promo WHERE promo.id = promo_id;

    RETURN count + cache_count;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE get_promo_availability(
	IN promo_id VARCHAR(255),
    IN account_id VARCHAR(255)
)
BEGIN
SELECT
	promo.allowed_claims_per_account,
    promo.allowed_claims_total,
    account_claim.claims as account_claims,
    calculate_total_promo_claims(promo_id) as total_claims
FROM promo, account_claim
WHERE promo.id = promo_id
	AND promo.id = account_claim.promo_id
    AND account_claim.account_id = account_id;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE get_promo(
	IN promo_id VARCHAR(255)
)
BEGIN
SELECT
	promo.allowed_claims_per_account,
    promo.allowed_claims_total,
    calculate_total_promo_claims(promo_id) as total_claims
FROM promo
WHERE promo.id = promo_id;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE claim_promo(
	IN promo_id VARCHAR(255),
    IN account_id VARCHAR(255)
)
BEGIN
    START TRANSACTION;
		INSERT INTO promo_claim (claim_number, promo_id) VALUES (0, promo_id);

		INSERT INTO account_claim
			(promo_id, account_id, claims)
		VALUES
			(promo_id, account_id, 1)
		ON DUPLICATE KEY UPDATE claims = claims + 1;
    COMMIT;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE reset_promo(
	IN promo_id VARCHAR(255)
)
BEGIN
	START TRANSACTION;
		DELETE FROM promo_claim WHERE promo_claim.promo_id = promo_id;

		SELECT claims FROM promo WHERE promo.id = promo_id FOR UPDATE;
		UPDATE promo SET claims = 0 WHERE promo.id = promo_id;

		SELECT claims FROM account_claim WHERE account_claim.promo_id = promo_id FOR UPDATE;
		UPDATE account_claim SET claims = 0 WHERE account_claim.promo_id = promo_id;
    COMMIT;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE update_promo_count(
	IN promo_id VARCHAR(255)
)
BEGIN
    DECLARE max_claim_number INT;
    DECLARE claim_count INT;

    SELECT MAX(claim_number) INTO max_claim_number FROM promo_claim;
    SELECT COUNT(claim_number) INTO claim_count FROM promo_claim WHERE promo_claim.promo_id = promo_id AND promo_claim.claim_number <= max_claim_number;

    START TRANSACTION;
		SELECT claims FROM promo WHERE promo.id = promo_id FOR UPDATE;
		UPDATE promo SET claims = claims + claim_count WHERE promo.id = promo_id;
		DELETE FROM promo_claim WHERE promo_claim.claim_number <= max_claim_number;
    COMMIT;
END$$
DELIMITER;

DELIMITER $$
CREATE EVENT IF NOT EXISTS scheduled_update_promo_cache
ON SCHEDULE EVERY 10 SECOND
DO
CALL update_promo_count("free-burgers");
