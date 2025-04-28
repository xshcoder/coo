CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS statistics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subject_id UUID NOT NULL,
    subject_type VARCHAR(10) NOT NULL CHECK (subject_type IN ('COO', 'REPLY')),
    replies_count INTEGER DEFAULT 0,
    likes_count INTEGER DEFAULT 0,
    views_count INTEGER DEFAULT 0,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_subject UNIQUE (subject_id, subject_type)
);

-- Create indexes for faster lookups
CREATE INDEX idx_statistics_subject_id ON statistics(subject_id);
CREATE INDEX idx_statistics_subject_type ON statistics(subject_type);

-- Create composite index for subject_id and subject_type
CREATE INDEX idx_statistics_subject ON statistics(subject_id, subject_type);