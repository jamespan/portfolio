package name.abuchen.portfolio.ui.views.dashboard;

import com.google.common.collect.Range;
import name.abuchen.portfolio.model.Dashboard.Widget;
import name.abuchen.portfolio.snapshot.trades.Trade;
import name.abuchen.portfolio.ui.views.trades.TradeDetailsView;
import name.abuchen.portfolio.util.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradesReturnWidget extends AbstractTradesWidget
{

    private final Range<Double> range;
    public TradesReturnWidget(Widget widget, DashboardData dashboardData, Range<Double> range)
    {
        super(widget, dashboardData);
        this.range = range;
    }

    @Override
    public void update(TradeDetailsView.Input input)
    {
        this.title.setText(TextUtil.tooltip(getWidget().getLabel()));

        List<Trade> trades = new ArrayList<>(input.getTrades());
        StringBuilder text = new StringBuilder();

        List<Trade> match = trades.stream().filter(trade -> range.contains(trade.getReturn()))
                .collect(Collectors.toList());
        text.append(match.size());
        this.indicator.setText(text.toString());
    }
}
