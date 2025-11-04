-- Manual Fix for PostgreSQL Auto-Increment Issue
-- Run this script in your PostgreSQL database to fix the note table id column

-- Step 1: Create the sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS note_id_seq;

-- Step 2: Set the sequence to start from the current max id + 1
-- This ensures no conflicts with existing data
-- Using CAST to handle type mismatch if id is stored as text
SELECT setval('note_id_seq', COALESCE((SELECT MAX(CAST(id AS INTEGER)) FROM note), 0) + 1, false);

-- Step 3: Change the column type to integer if needed
-- This converts any text values to integers
ALTER TABLE note ALTER COLUMN id TYPE INTEGER USING CAST(id AS INTEGER);

-- Step 4: Set the default value for the id column to use the sequence
ALTER TABLE note ALTER COLUMN id SET DEFAULT nextval('note_id_seq');

-- Step 5: Associate the sequence with the column
-- This ensures the sequence is dropped when the table is dropped
ALTER SEQUENCE note_id_seq OWNED BY note.id;

-- Verify the fix
SELECT column_name, column_default, data_type
FROM information_schema.columns
WHERE table_name = 'note' AND column_name = 'id';

-- Expected output:
-- data_type should be 'integer'
-- column_default should be nextval('note_id_seq'::regclass)

