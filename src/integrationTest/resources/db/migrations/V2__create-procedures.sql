SET GLOBAL log_bin_trust_function_creators = 1;

DELIMITER $$
CREATE FUNCTION get_promo_total_claims(
    promo_id VARCHAR(255)
)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total_claims INT;
    SELECT COUNT(claim_number) INTO total_claims FROM promo_claim WHERE promo_claim.promo_id = promo_id;
    return total_claims;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE get_promo_availability(
	IN promo_id VARCHAR(255),
    IN account_id VARCHAR(255)
)
BEGIN
SELECT promo.claims_allowed_per_account,
    promo.total_allowed_claims,
    account_claim.claims as account_claims,
    get_promo_total_claims(promo_id) as total_claims
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
	promo.id,
	promo.claims_allowed_per_account,
    promo.total_allowed_claims,
    get_promo_total_claims(promo_id) as total_claims
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
		DELETE FROM promo_claim
            WHERE promo_claim.promo_id = promo_id;

		SELECT claims
			FROM account_claim
            WHERE account_claim.promo_id = promo_id FOR UPDATE;
		UPDATE account_claim
			SET claims = 0
            WHERE account_claim.promo_id = promo_id;
    COMMIT;
END$$