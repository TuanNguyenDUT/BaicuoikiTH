#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Script import dữ liệu Excel vào PostgreSQL
Hỗ trợ 2 file: danhsachphongthi.xlsx và danhsachcanbocoithi.xlsx
"""

import pandas as pd
from sqlalchemy import create_engine, text
import sys

# Thông tin kết nối PostgreSQL
DATABASE_URL = "postgresql://neondb_owner:npg_zbGepchkdQ78@ep-solitary-butterfly-aq5mpxtd-pooler.c-8.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require"

# Đường dẫn file Excel
EXCEL_FILES = {
    'phongthi': 'input/danhsachphongthi.xlsx',
    'canbocoithi': 'input/danhsachcanbocoithi.xlsx'
}

def import_excel_to_postgres():
    """Import dữ liệu từ Excel vào PostgreSQL"""
    
    try:
        # Kết nối tới database
        print("🔌 Kết nối tới PostgreSQL...")
        engine = create_engine(DATABASE_URL)
        
        # Test kết nối
        with engine.connect() as connection:
            connection.execute(text("SELECT 1"))
            print("✅ Kết nối thành công!\n")
        
        # Import file 1: Danh sách phòng thi
        print("📥 Import danhsachphongthi.xlsx...")
        df_phongthi = pd.read_excel(EXCEL_FILES['phongthi'])
        
        # Chuẩn hóa tên cột (xóa khoảng trắng)
        df_phongthi.columns = df_phongthi.columns.str.strip()
        
        print(f"   - Số dòng: {len(df_phongthi)}")
        print(f"   - Tên cột: {list(df_phongthi.columns)}")
        
        # Insert vào database
        df_phongthi.to_sql(
            'phongthi',
            con=engine,
            if_exists='replace',  # Xóa bảng cũ nếu tồn tại
            index=False,
            method='multi'
        )
        print(f"   ✅ Import thành công: {len(df_phongthi)} dòng\n")
        
        # Import file 2: Danh sách cán bộ coi thi
        print("📥 Import danhsachcanbocoithi.xlsx...")
        df_canbo = pd.read_excel(EXCEL_FILES['canbocoithi'])
        
        # Chuẩn hóa tên cột
        df_canbo.columns = df_canbo.columns.str.strip()
        
        print(f"   - Số dòng: {len(df_canbo)}")
        print(f"   - Tên cột: {list(df_canbo.columns)}")
        
        # Insert vào database
        df_canbo.to_sql(
            'canbocoithi',
            con=engine,
            if_exists='replace',
            index=False,
            method='multi'
        )
        print(f"   ✅ Import thành công: {len(df_canbo)} dòng\n")
        
        # Thống kê kết quả
        print("=" * 50)
        print("📊 THỐNG KÊ IMPORT")
        print("=" * 50)
        print(f"✅ Bảng 'phongthi': {len(df_phongthi)} dòng")
        print(f"✅ Bảng 'canbocoithi': {len(df_canbo)} dòng")
        print(f"✅ Tổng cộng: {len(df_phongthi) + len(df_canbo)} dòng")
        print("=" * 50)
        print("\n🎉 Import dữ liệu hoàn tất!")
        
        engine.dispose()
        return True
        
    except Exception as e:
        print(f"\n❌ Lỗi: {str(e)}")
        import traceback
        traceback.print_exc()
        return False

if __name__ == "__main__":
    success = import_excel_to_postgres()
    sys.exit(0 if success else 1)
