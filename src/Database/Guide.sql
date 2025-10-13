USE TRAVELBOOKINGSYSTEMAPP;

-- CREATE TABLE guide_Sequence (
--     Last_ID INT NOT NULL
-- );
-- Initialize sequence only if table is empty to avoid duplicate rows on re-import
INSERT INTO guide_Sequence (Last_ID)
SELECT 0 WHERE NOT EXISTS (SELECT * FROM guide_Sequence);

-- Bước 3: Tạo Trigger
CREATE TRIGGER tg_guide_insert
BEFORE INSERT ON Guide
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    -- Lấy và cập nhật số đếm trong một giao dịch an toàn
    UPDATE guide_Sequence SET Last_ID = Last_ID + 1 WHERE Last_ID >= 0;
    SET next_id = (SELECT Last_ID FROM guide_Sequence);
    -- Gán ID mới cho bản ghi đang được chèn
    SET NEW.Id = CONCAT('HD', LPAD(next_id, 3, '0'));
END;
