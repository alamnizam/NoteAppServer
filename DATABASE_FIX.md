# Database Fix for Auto-Increment Issue

## Problem
The PostgreSQL database was throwing an error:
```
PSQLException: ERROR: null value in column "id" of relation "note" violates not-null constraint
```

This occurred because the `note` table's `id` column wasn't properly configured with auto-increment functionality.

## Solution Applied

### 1. **Updated NoteTable.kt**
- Added explicit table name `"note"` to ensure proper table mapping
- Ensured `autoIncrement()` is properly configured on the `id` column

### 2. **Updated UserTable.kt**
- Added explicit table name `"User"` for consistency
- Fixed user duplicate key issue by checking if user exists before insertion

### 3. **Updated DatabaseFactory.kt**
- Added automatic migration logic that runs on application startup
- The migration:
  - Creates a sequence `note_id_seq` if it doesn't exist
  - Sets the sequence to start from the current maximum id + 1
  - Configures the `id` column to use the sequence as default value
  - Associates the sequence with the column

### 4. **Updated UserDaoImpl.kt**
- Added check to prevent duplicate user registration

## How It Works

When the application starts, the `DatabaseFactory.init()` method will:
1. Create tables if they don't exist
2. Run a PostgreSQL migration script that fixes the auto-increment issue
3. The migration is idempotent (safe to run multiple times)

## Next Steps

Simply restart your application. The migration will run automatically and fix the issue. After restart:
- New notes will automatically get sequential IDs
- User registration will properly handle duplicate emails
- No manual database intervention is required

## Manual Database Fix (Alternative)

If you prefer to manually fix the database, you can run this SQL in PostgreSQL:

```sql
-- Create sequence if it doesn't exist
CREATE SEQUENCE IF NOT EXISTS note_id_seq;

-- Set the sequence to start from the max id + 1
SELECT setval('note_id_seq', COALESCE((SELECT MAX(id) FROM note), 0) + 1, false);

-- Set the default value for id column to use the sequence
ALTER TABLE note ALTER COLUMN id SET DEFAULT nextval('note_id_seq');

-- Associate the sequence with the column
ALTER SEQUENCE note_id_seq OWNED BY note.id;
```

## Verification

After restart, verify the fix by:
1. Creating a new note through the API
2. The note should be created successfully with an auto-generated ID
3. No more null constraint violations should occur

