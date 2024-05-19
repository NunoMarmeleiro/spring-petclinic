/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 90.47619047619048, "KoPercent": 9.523809523809524};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.02773809523809524, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.1076, 500, 1500, "CSS"], "isController": false}, {"data": [0.0076, 500, 1500, "Owner"], "isController": false}, {"data": [2.0E-4, 500, 1500, "POST Edit Owner"], "isController": false}, {"data": [0.0022, 500, 1500, "POST new visit-0"], "isController": false}, {"data": [1.0E-4, 500, 1500, "New Pet"], "isController": false}, {"data": [0.0036, 500, 1500, "POST new visit-1"], "isController": false}, {"data": [0.0, 500, 1500, "DELETE Pet"], "isController": false}, {"data": [0.1148, 500, 1500, "Vets"], "isController": false}, {"data": [0.1114, 500, 1500, "Home page"], "isController": false}, {"data": [0.1054, 500, 1500, "JS"], "isController": false}, {"data": [0.1024, 500, 1500, "Find owner"], "isController": false}, {"data": [0.0164, 500, 1500, "Find owner with lastname=\"\""], "isController": false}, {"data": [1.0E-4, 500, 1500, "POST new pet-0"], "isController": false}, {"data": [0.0019, 500, 1500, "New visit"], "isController": false}, {"data": [7.0E-4, 500, 1500, "POST new pet-1"], "isController": false}, {"data": [0.0, 500, 1500, "Delete Pet"], "isController": false}, {"data": [0.0, 500, 1500, "POST new visit"], "isController": false}, {"data": [0.0, 500, 1500, "POST new pet"], "isController": false}, {"data": [0.0049, 500, 1500, "Edit Owner"], "isController": false}, {"data": [0.0022, 500, 1500, "POST Edit Owner-0"], "isController": false}, {"data": [0.001, 500, 1500, "POST Edit Owner-1"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 105000, 10000, 9.523809523809524, 6694.836352380931, 0, 27596, 8029.0, 17712.0, 20176.850000000002, 23319.800000000032, 100.59889877738804, 57485.60848920573, 20.827115762506118], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["CSS", 5000, 0, 0.0, 2930.226200000002, 1, 8564, 2803.0, 4895.0, 5727.9, 7284.9299999999985, 5.0728820970888755, 1429.0764634250272, 0.7084200584801847], "isController": false}, {"data": ["Owner", 5000, 0, 0.0, 7634.601400000011, 291, 18788, 7382.0, 11351.900000000001, 12559.099999999997, 13872.609999999991, 4.936126522795032, 7875.576986914328, 0.5977340711197109], "isController": false}, {"data": ["POST Edit Owner", 5000, 0, 0.0, 14807.36919999998, 1445, 27596, 14674.0, 20131.30000000002, 21330.9, 24127.98, 4.869141813755325, 7877.918571211199, 2.4535909920876446], "isController": false}, {"data": ["POST new visit-0", 5000, 0, 0.0, 7182.363599999999, 824, 16847, 7176.0, 10119.900000000001, 11321.799999999996, 13030.669999999993, 4.883589868309115, 0.8965965773848767, 1.4927379187312042], "isController": false}, {"data": ["New Pet", 5000, 0, 0.0, 7497.862999999988, 1235, 17683, 7265.0, 10628.500000000004, 11933.95, 13445.99, 4.8592281601990335, 23.147768520948134, 0.8826332400361526], "isController": false}, {"data": ["POST new visit-1", 5000, 0, 0.0, 7630.243600000004, 403, 16205, 7480.5, 11077.0, 12182.95, 13439.99, 4.905861425113497, 8244.57257651672, 0.8479858127393447], "isController": false}, {"data": ["DELETE Pet", 5000, 5000, 100.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 4.948520540813913, 5.818377667128859, 0.0], "isController": false}, {"data": ["Vets", 5000, 0, 0.0, 2826.7377999999953, 1, 8385, 2632.5, 4949.700000000002, 5980.95, 7332.99, 5.042518516127991, 20.997362258564213, 0.6155418110507802], "isController": false}, {"data": ["Home page", 5000, 0, 0.0, 3058.206000000008, 1, 8512, 2893.0, 5293.900000000001, 6101.849999999999, 7505.889999999998, 5.10218138663004, 15.889508244104686, 0.5779814852041842], "isController": false}, {"data": ["JS", 5000, 0, 0.0, 2855.479199999999, 0, 8422, 2765.5, 4763.700000000002, 5750.099999999997, 7595.829999999996, 5.056552482969531, 400.01083050334944, 0.8444047603396385], "isController": false}, {"data": ["Find owner", 5000, 0, 0.0, 2886.8492000000006, 1, 9026, 2661.5, 5059.0, 6029.95, 7647.939999999999, 5.025666076653469, 18.429078240072048, 0.6233003825537017], "isController": false}, {"data": ["Find owner with lastname=\"\"", 5000, 0, 0.0, 6594.510600000011, 25, 16094, 6309.5, 10105.7, 11681.8, 13302.939999999999, 4.970480317394991, 553.572380507168, 0.6407259784141981], "isController": false}, {"data": ["POST new pet-0", 5000, 0, 0.0, 7270.473400000016, 787, 18000, 7161.5, 10112.200000000004, 11262.199999999997, 13422.99, 4.856844508123072, 0.8916862964132204, 1.5130208965734964], "isController": false}, {"data": ["New visit", 5000, 0, 0.0, 7473.040800000026, 643, 17258, 7367.0, 10570.500000000004, 11658.9, 13080.98, 4.876145894285157, 21.037902891554513, 0.9285629388531303], "isController": false}, {"data": ["POST new pet-1", 5000, 0, 0.0, 7933.2008000000005, 572, 18296, 7751.0, 11000.7, 12239.95, 13848.939999999999, 4.8607965095592425, 8037.1640209857605, 0.8401962716718613], "isController": false}, {"data": ["Delete Pet", 5000, 5000, 100.0, 6.0E-4, 0, 1, 0.0, 0.0, 0.0, 0.0, 4.9485254383898685, 5.818383425606838, 0.0], "isController": false}, {"data": ["POST new visit", 5000, 0, 0.0, 14811.665799999997, 2082, 27002, 14971.5, 19736.9, 21030.5, 23348.949999999997, 4.880586685564298, 8202.993018259738, 2.3354369882094783], "isController": false}, {"data": ["POST new pet", 5000, 0, 0.0, 15202.748200000011, 4139, 27150, 15132.5, 19806.500000000004, 21026.9, 23631.58999999999, 4.845341543280044, 8012.499326951775, 2.3469623100262713], "isController": false}, {"data": ["Edit Owner", 5000, 0, 0.0, 7187.765800000013, 114, 19604, 6870.0, 10830.0, 12302.9, 13522.98, 4.915541171589745, 26.617847457485485, 0.6192429796240987], "isController": false}, {"data": ["POST Edit Owner-0", 5000, 0, 0.0, 6941.111999999993, 395, 16808, 6634.0, 10140.0, 11720.349999999995, 13447.97, 4.890783904625802, 1.4662799401563682, 1.4089660662740344], "isController": false}, {"data": ["POST Edit Owner-1", 5000, 0, 0.0, 7867.106200000001, 431, 18246, 7596.5, 11138.100000000006, 12684.099999999997, 14288.98, 4.8710534724766, 7879.55113181363, 1.0512722826341099], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/3/pets/petCount]/delete", 3332, 33.32, 3.1733333333333333], "isController": false}, {"data": ["Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/2/pets/petCount]/delete", 3334, 33.34, 3.1752380952380954], "isController": false}, {"data": ["Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/1/pets/petCount]/delete", 3334, 33.34, 3.1752380952380954], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 105000, 10000, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/2/pets/petCount]/delete", 3334, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/1/pets/petCount]/delete", 3334, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/3/pets/petCount]/delete", 3332, "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["DELETE Pet", 5000, 5000, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/2/pets/petCount]/delete", 1667, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/1/pets/petCount]/delete", 1667, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/3/pets/petCount]/delete", 1666, "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Delete Pet", 5000, 5000, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/2/pets/petCount]/delete", 1667, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/1/pets/petCount]/delete", 1667, "Non HTTP response code: java.net.URISyntaxException/Non HTTP response message: Illegal character in path at index 44: http://localhost:8080/owners/3/pets/petCount]/delete", 1666, "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
