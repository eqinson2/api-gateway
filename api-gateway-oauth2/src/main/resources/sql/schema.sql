CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(40) DEFAULT NULL,
  token BLOB,
  authentication_id VARCHAR(256) DEFAULT NULL,
  user_name VARCHAR(256) DEFAULT NULL,
  client_id VARCHAR(256) DEFAULT NULL,
  authentication BLOB,
  refresh_token VARCHAR(256) DEFAULT NULL,
  PRIMARY KEY (token_id)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(40) DEFAULT NULL,
  token BLOB,
  authentication BLOB,
  PRIMARY KEY (token_id)
);