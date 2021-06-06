db.createUser(
    {
        user: "promo-user",
        pwd: "welcome01",
        roles: [ { role: "readWrite", db: "promo-local" } ]
    }
);