package project.prm392_oss.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "ROLE")
public class Role {
    @PrimaryKey(autoGenerate = true)
    public int role_id;
    public String name;

    public Role() {}

    public Role(int role_id, String name) {
        this.role_id = role_id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;  // Trả về tên vai trò khi gọi toString()
    }
    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

