CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS replies (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    coo_id UUID REFERENCES coos(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    replied_to_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    replied_to_reply_id UUID REFERENCES replies(id) ON DELETE SET NULL
);

-- Create indexes for faster lookups
CREATE INDEX idx_replies_coo_id ON replies(coo_id);
CREATE INDEX idx_replies_user_id ON replies(user_id);
CREATE INDEX idx_replies_replied_to_reply_id ON replies(replied_to_reply_id);