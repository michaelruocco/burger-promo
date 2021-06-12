CREATE TABLE IF NOT EXISTS promo(
    id VARCHAR(255) PRIMARY KEY,
    claims_allowed_per_account INT,
    total_allowed_claims INT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS account(
   	id VARCHAR(255) PRIMARY KEY
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS account_claim(
   	promo_id VARCHAR(255),
   	account_id VARCHAR(255),
	claims INT,
	PRIMARY KEY(promo_id, account_id),
	FOREIGN KEY(promo_id)
        REFERENCES promo(id)
        ON DELETE CASCADE,
	FOREIGN KEY(account_id)
        REFERENCES account(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS promo_claim(
   	claim_number int PRIMARY KEY AUTO_INCREMENT,
   	promo_id VARCHAR(255),
   	claimed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY(promo_id)
        REFERENCES promo(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX claim_promo_id_index ON promo_claim(promo_id);

DELIMITER $$
CREATE FUNCTION get_promo_total_claims(
    promo_id VARCHAR(255)
)
RETURNS INT
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

CALL get_promo("free-burgers");
CALL claim_promo("free-burgers", "2");
CALL reset_promo("free-burgers");

SELECT get_promo_total_claims("free-burgers");

INSERT INTO promo (id, claims_allowed_per_account, total_allowed_claims)
VALUES ("free-burgers", 1, 90000);

INSERT INTO account (id)
VALUES ("1"), ("2"), ("3");