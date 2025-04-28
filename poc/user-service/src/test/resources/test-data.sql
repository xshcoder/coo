-- Test data for user repository tests
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    logo VARCHAR(255),
    handle VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);