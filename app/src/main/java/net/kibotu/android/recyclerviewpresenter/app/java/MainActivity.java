package net.kibotu.android.recyclerviewpresenter.app.java;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import kotlin.Unit;
import net.kibotu.android.recyclerviewpresenter.PresenterAdapter;
import net.kibotu.android.recyclerviewpresenter.RecyclerViewModel;
import net.kibotu.android.recyclerviewpresenter.app.R;
import net.kibotu.android.recyclerviewpresenter.app.kotlin.PaginationActivity;
import net.kibotu.android.recyclerviewpresenter.app.misc.FakeDataGenerator;

import java.util.UUID;

import static java.text.MessageFormat.format;
import static net.kibotu.logger.Logger.toast;

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runJavaImplementation();
//        runKotlinImplementation();
//        runPagination();
    }

    private void runJavaImplementation() {
        RecyclerView list = findViewById(R.id.list);

        PresenterAdapter<RecyclerViewModel<String>> adapter = new PresenterAdapter<>();
        list.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        list.setAdapter(adapter);

        adapter.onItemClick((item, rowView, position) -> {
            toast(format("{0}. {1}", position, item));
            return Unit.INSTANCE;
        });

        for (int i = 0; i < 100; ++i) {
            RecyclerViewModel<String> viewModel = new RecyclerViewModel<>(FakeDataGenerator.INSTANCE.createRandomImageUrl(), UUID.randomUUID().toString(), null);
            adapter.append(viewModel, PhotoPresenter.class);
            adapter.append(viewModel, LabelPresenter.class);
        }

//        adapter.update(0, "https://raw.githubusercontent.com/kibotu/RecyclerViewPresenter/master/screenshot.png");

        adapter.notifyDataSetChanged();
    }

    private void runPagination() {
        startActivity(new Intent(this, PaginationActivity.class));
    }

    private void runKotlinImplementation() {
        startActivity(new Intent(this, net.kibotu.android.recyclerviewpresenter.app.kotlin.MainActivity.class));
        finish();
    }
}