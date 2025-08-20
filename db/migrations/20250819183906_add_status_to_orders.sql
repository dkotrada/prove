-- migrate:up
ALTER TABLE orders ADD COLUMN status TEXT NOT NULL DEFAULT 'PENDING';

-- Update existing rows (if any) to ensure no NULL values
UPDATE orders SET status = 'PENDING' WHERE status IS NULL;

-- migrate:down
ALTER TABLE orders DROP COLUMN status;