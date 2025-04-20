-- Create indexes for search functionality

-- Enable pg_trgm extension if not already enabled
CREATE EXTENSION IF NOT EXISTS pg_trgm;
-- Users table indexes for handle and name search
CREATE INDEX IF NOT EXISTS idx_users_handle_search ON users USING gin (LOWER(handle) gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_users_name_search ON users USING gin (LOWER(name) gin_trgm_ops);

-- Coos table index for content search
CREATE INDEX IF NOT EXISTS idx_coos_content_search ON coos USING gin (LOWER(content) gin_trgm_ops);
