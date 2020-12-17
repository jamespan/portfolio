package name.abuchen.portfolio.ui.views.dashboard;

import com.ibm.icu.text.MessageFormat;
import name.abuchen.portfolio.model.Dashboard.Widget;
import name.abuchen.portfolio.snapshot.trades.Trade;
import name.abuchen.portfolio.ui.views.trades.TradeDetailsView;
import name.abuchen.portfolio.util.TextUtil;

import java.util.List;

public class TradesAverageReturnRatioWidget extends AbstractTradesWidget
{

    public static final double THRESHOLD = 2.0 / 100;

    public TradesAverageReturnRatioWidget(Widget widget, DashboardData dashboardData)
    {
        super(widget, dashboardData);
    }

    @Override
    public void update(TradeDetailsView.Input input)
    {
        this.title.setText(TextUtil.tooltip(getWidget().getLabel()));

        List<Trade> trades = input.getTrades();
        double threshold = THRESHOLD;
        double positive = trades.stream().mapToDouble(Trade::getReturn).filter(d -> Math.abs(d) > threshold).filter(r -> r >= 0).average().orElse(0);
        double negative = trades.stream().mapToDouble(Trade::getReturn).filter(d -> Math.abs(d) > threshold).filter(r -> r < 0).average().orElse(0);
        double total = trades.stream().mapToDouble(Trade::getReturn).filter(d -> Math.abs(d) > threshold).average().orElse(0);
        String text = MessageFormat.format("{0} <green>↑{1}</green> <red>↓{2}</red>",
                String.format("%.2f%%", total * 100),
                String.format("%.2f%%", positive * 100),
                String.format("%.2f%%", negative * 100));

        this.indicator.setText(text);
    }
}
