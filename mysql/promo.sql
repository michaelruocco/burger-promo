CREATE TABLE IF NOT EXISTS promo(
    id VARCHAR(255),
    claims_allowed_per_account INT,
    total_allowed_claims INT,
    total_claims INT,
    PRIMARY KEY(id)
) ENGINE=InnoDB;

INSERT INTO promo VALUES
("free-burgers", 1, 90000, 0);

CREATE TABLE IF NOT EXISTS account(
   	id VARCHAR(255),
	PRIMARY KEY(id)
) ENGINE=InnoDB;

INSERT INTO account VALUES
("1"),
("2"),
("3");

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

INSERT INTO account_claim VALUES
("free-burgers", "1", 1),
("free-burgers", "2", 0),
("free-burgers", "3", 2);

DELIMITER $$
CREATE PROCEDURE get_promo_availability(
	IN promo_id VARCHAR(255),
    IN account_id VARCHAR(255)
)
BEGIN
SELECT account_claim.claims as account_claims,
    promo.claims_allowed_per_account,
    promo.total_claims,
    promo.total_allowed_claims
FROM promo, account_claim
WHERE promo.id = account_claim.promo_id
    AND promo.id = promo_id
    AND account_claim.account_id = account_id;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE claim_promo(
	IN promo_id VARCHAR(255),
    IN account_id VARCHAR(255)
)
BEGIN
    START TRANSACTION;
		SELECT total_claims
			FROM promo
			WHERE id = promo_id FOR UPDATE;
		UPDATE promo
			SET total_claims = total_claims + 1
			WHERE promo.id = promo_id;

        IF EXISTS(
			SELECT *
			FROM account_claim
			WHERE account_claim.account_id = account_id
			AND account_claim.promo_id = promo_id)
		THEN
			SELECT claims
				FROM account_claim
				WHERE account_claim.account_id = account_id
				AND account_claim.promo_id = promo_id FOR UPDATE;
			UPDATE account_claim
				SET claims = claims + 1
				WHERE account_claim.account_id = account_id
				AND account_claim.promo_id = promo_id;
		ELSE
			INSERT IGNORE INTO account_claim
				(promo_id, account_id, claims)
			VALUES
				(promo_id, account_id, 1);
		END IF;
    COMMIT;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE reset_promo(
	IN promo_id VARCHAR(255)
)
BEGIN
    START TRANSACTION;
		SELECT total_claims
			FROM promo
            WHERE id = promo_id FOR UPDATE;
        UPDATE promo
			SET total_claims = 0
            WHERE id = promo_id;

		SELECT claims
			FROM account_claim
            WHERE account_claim.promo_id = promo_id FOR UPDATE;
		UPDATE account_claim
			SET claims = 0
            WHERE account_claim.promo_id = promo_id;
    COMMIT;
END$$
DELIMITER ;

CALL get_promo_availability("free-burgers", "2");
CALL claim_promo("free-burgers", "1");
CALL reset_promo("free-burgers");