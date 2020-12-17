package name.abuchen.portfolio.ui.views.dashboard;

import com.ibm.icu.text.MessageFormat;
import name.abuchen.portfolio.model.Dashboard.Widget;
import name.abuchen.portfolio.money.Money;
import name.abuchen.portfolio.money.MoneyCollectors;
import name.abuchen.portfolio.money.Values;
import name.abuchen.portfolio.snapshot.trades.Trade;
import name.abuchen.portfolio.ui.views.trades.TradeDetailsView;
import name.abuchen.portfolio.util.TextUtil;

import java.util.List;
import java.util.stream.Collectors;

import static name.abuchen.portfolio.ui.views.dashboard.TradesAverageReturnRatioWidget.THRESHOLD;

public class TradesAverageProfitLossWidget extends AbstractTradesWidget
{
    public TradesAverageProfitLossWidget(Widget widget, DashboardData dashboardData)
    {
        super(widget, dashboardData);
    }

    @Override
    public void update(TradeDetailsView.Input input)
    {
        this.title.setText(TextUtil.tooltip(getWidget().getLabel()));

        List<Trade> trades = input.getTrades().stream().filter(trade -> Math.abs(trade.getReturn()) > THRESHOLD).collect(Collectors.toList());
        List<Money> profits = trades.stream().map(Trade::getProfitLoss).filter(Money::isPositive).collect(Collectors.toList());
        List<Money> losses = trades.stream().map(Trade::getProfitLoss).filter(Money::isNegative).collect(Collectors.toList());
        Money avgProfit = profits.stream().collect(MoneyCollectors.sum(getDashboardData().getCurrencyConverter().getTermCurrency())).divide(Math.max(1, profits.size()));
        Money avgLoss = losses.stream().collect(MoneyCollectors.sum(getDashboardData().getCurrencyConverter().getTermCurrency())).divide(Math.max(1, losses.size()));
        Money avgTotal = trades.stream().map(Trade::getProfitLoss).collect(MoneyCollectors.sum(getDashboardData().getCurrencyConverter().getTermCurrency())).divide(Math.max(1, trades.size()));
        this.indicator.setText(
                        MessageFormat.format("{0} <green>↑{1}</green> <red>↓{2}</red>", //$NON-NLS-1$ //$NON-NLS-2$
                                Values.Money.format(avgTotal, getClient().getBaseCurrency()),
                                        Values.Money.format(avgProfit, getClient().getBaseCurrency()),
                                Values.Money.format(avgLoss, getClient().getBaseCurrency())));
    }
}
