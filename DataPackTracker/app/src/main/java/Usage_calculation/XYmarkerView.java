package Usage_calculation;

import android.content.Context;
import android.widget.TextView;

import com.example.datapacktracker.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

public class XYmarkerView extends MarkerView {

    private final TextView tvContent;
    private final DecimalFormat format;

    public XYmarkerView(Context context) {
        super(context, R.layout.xy_marker_view);
        tvContent = findViewById(R.id.tvContent);
        format = new DecimalFormat("###.#");
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(String.format("%s GB", format.format(e.getY())));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
