package project.prm392_oss.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import project.prm392_oss.dao.FeedbackDAO;
import project.prm392_oss.database.AppDatabase;
import project.prm392_oss.entity.Feedback;

public class FeedbackRepository {
    private final FeedbackDAO feedbackDAO;
    public FeedbackRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        feedbackDAO = db.feedbackDAO();
    }
    public void insert(Feedback feedback) { AppDatabase.databaseWriteExecutor.execute(() -> feedbackDAO.insert(feedback)); }
    public void update(Feedback feedback) { AppDatabase.databaseWriteExecutor.execute(() -> feedbackDAO.update(feedback)); }
    public void delete(Feedback feedback) { AppDatabase.databaseWriteExecutor.execute(() -> feedbackDAO.delete(feedback)); }
    public LiveData<List<Feedback>> getAllFeedbacks() { return feedbackDAO.getAllFeedbacks(); }
}

