CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    handle VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    bio TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS coos (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    content VARCHAR(280) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS replies (
    id UUID PRIMARY KEY,
    coo_id UUID NOT NULL REFERENCES coos(id),
    user_id UUID NOT NULL REFERENCES users(id),
    content VARCHAR(280) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS likes (
    id UUID PRIMARY KEY,
    coo_id UUID REFERENCES coos(id),
    reply_id UUID REFERENCES replies(id),
    user_id UUID NOT NULL REFERENCES users(id),
    liked_to_user_id UUID NOT NULL REFERENCES users(id),
    liked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT like_target_check CHECK (
        (coo_id IS NOT NULL AND reply_id IS NULL) OR
        (coo_id IS NULL AND reply_id IS NOT NULL)
    )
);

CREATE TABLE IF NOT EXISTS statistics (
    id UUID PRIMARY KEY,
    subject_id UUID NOT NULL,
    subject_type VARCHAR(10) NOT NULL CHECK (subject_type IN ('COO', 'REPLY')),
    replies_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_subject UNIQUE (subject_id, subject_type)
);