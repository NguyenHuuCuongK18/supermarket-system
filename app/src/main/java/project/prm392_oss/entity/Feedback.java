package project.prm392_oss.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FEEDBACK")
public class Feedback {

    @PrimaryKey(autoGenerate = true)
    public int feedback_id;
    public int customer_id;
    public int product_id;
    public int point;
    public String image;
    public String description;
    public Feedback() {
    }

    public Feedback(int feedback_id, int customer_id, int product_id, int point, String image, String description) {
        this.feedback_id = feedback_id;
        this.customer_id = customer_id;
        this.product_id = product_id;
        this.point = point;
        this.image = image;
        this.description = description;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
