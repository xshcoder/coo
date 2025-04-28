-- Create indexes for personalize timeline queries

-- Index for coos table to optimize timeline sorting
CREATE INDEX IF NOT EXISTS idx_coos_created_at ON coos(created_at DESC);

-- Composite index for coos to optimize timeline filtering and sorting
CREATE INDEX IF NOT EXISTS idx_coos_user_created_at ON coos(user_id, created_at DESC);

-- Index for replies table to optimize timeline sorting
CREATE INDEX IF NOT EXISTS idx_replies_created_at ON replies(created_at DESC);

-- Composite index for replies to optimize timeline filtering and sorting
CREATE INDEX IF NOT EXISTS idx_replies_coo_created_at ON replies(coo_id, created_at DESC);