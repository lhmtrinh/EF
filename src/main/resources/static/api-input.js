var i;
axios.get('http://localhost:8080//ef?tickers=NFLX,AMZN,AAPL&portfolios=1000').then(resp => {
    dataEF = resp.data;
    console.log(dataEF);
    addDataToTable(dataEF);
});
function addDataToTable(dataEF) {
    var table = document.getElementById('datatable');
    var tr =document.createElement('tr');
    tr.innerHTML ='<th rowspan="2" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Risk</th>'+
    '<th rowspan="2" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">Return</th>';
    for (ticker in dataEF['tickers']){
        tr.innerHTML += '<th rowspan="2" class="headerSort" tabindex="0" role="columnheader button" title="Sort ascending">'+dataEF['tickers'][ticker]+'(%) </th>';
    }
    var tableHeader = document.getElementById('tableHeader');
    tableHeader.innerHTML = tr.innerHTML;
    console.log(tableHeader);
    for(point in dataEF['curveList']) {
        tr = document.createElement('tr');
        var returnRate =dataEF['curveList'][point]['return'];
        var riskRate =dataEF['curveList'][point]['risk'];
        var weights = dataEF['curveList'][point]['weights'];
        tr.innerHTML = '<td>' +riskRate+'</td>'+'<td>'+returnRate+'</td>';
        for (key in weights){
            tr.innerHTML +='<td>'+weights[key]+'</td>';
        }
        table.appendChild(tr);
    }
    creatChart();
}
function creatChart(){
    Highcharts.chart("container", {
        data: {
            table: "datatable",
            startRow: 0,
            startColumn: 0,
            endColumn: 1
        },
        chart: {
            type: "scatter"
        },
        title: {
            text: "Efficient Frontier"
        },
        yAxis: {
            title: {
            text: "Return rate (%)"
            }
        },
        legend: {
            enabled: false
        },
        xAxis: {
            title: {
            text: "Risk rate (%)"
            }
        },
        tooltip: {
            pointFormat: "Risk: <b>{point.x}</b><br> Return: <b>{point.y}</b>"
        }
    });
}