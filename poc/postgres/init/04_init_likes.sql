CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS likes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coo_id UUID REFERENCES coos(id) ON DELETE CASCADE,
    reply_id UUID REFERENCES replies(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    liked_to_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    liked_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT likes_target_check CHECK (
        (coo_id IS NOT NULL AND reply_id IS NULL) OR
        (coo_id IS NULL AND reply_id IS NOT NULL)
    )
);

-- Create indexes for faster lookups
CREATE INDEX idx_likes_coo_id ON likes(coo_id);
CREATE INDEX idx_likes_reply_id ON likes(reply_id);
CREATE INDEX idx_likes_user_id ON likes(user_id);
CREATE INDEX idx_likes_liked_to_user_id ON likes(liked_to_user_id);

-- Create unique constraints to prevent duplicate likes
CREATE UNIQUE INDEX idx_unique_coo_like ON likes(coo_id, user_id) WHERE coo_id IS NOT NULL;
CREATE UNIQUE INDEX idx_unique_reply_like ON likes(reply_id, user_id) WHERE reply_id IS NOT NULL;