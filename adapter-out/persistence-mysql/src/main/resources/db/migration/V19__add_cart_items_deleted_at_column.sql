-- V19: Add deleted_at column to cart_items table for soft delete support
-- Payment flow: cart items are soft-deleted when payment starts, restored if payment fails

ALTER TABLE cart_items
    ADD COLUMN deleted_at TIMESTAMP NULL AFTER added_at;

-- Add index for efficient filtering of active items
CREATE INDEX idx_cart_items_deleted_at ON cart_items (deleted_at);

-- Composite index for common query patterns (cart + active items)
CREATE INDEX idx_cart_items_cart_id_deleted_at ON cart_items (cart_id, deleted_at);
