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