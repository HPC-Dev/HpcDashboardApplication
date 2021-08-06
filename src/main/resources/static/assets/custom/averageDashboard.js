dataTable();
var table;

function dataTable() {

    var buttonCommon = {
        exportOptions: {
            format: {
                body: function(data, row, column, node) {
                    return data;
                }
            }
        }
    };

    table = $('table#average').DataTable({
        'ajax': '/datatable/averageDashboard',
        'scrollY': "520px",
        'scrollX': true,
        'destroy': true,
        'scrollCollapse': true,
        'processing': true,
        'serverSide': true,
        "dom": 'lBfrtip',
        'lengthMenu': [
            [10, 25, 50, 100, -1],
            [10, 25, 50, 100, "All"]
        ],
        'buttons': [{
            extend: 'collection',
            text: 'Export',
            buttons: [
                'copy',
                'excel', 'csv'
            ]
        }],

        columns: [{
                data: 'appName'
            },
            {
                data: 'bmName'
            },
            {
                data: 'nodes'
            },
            {
                data: 'cpuSku'
            },
            {
                data: 'cores'
            },
            {
                data: 'avgResult',
                searchable: false,
                "render": function(data, type, full) {
                    return data.toString().match(/\d+(\.\d{1,2})?/g)[0];
                }
            },
            {
                data: 'perCorePerf',
                searchable: false,
                "render": function(data, type, full) {
                    return data.toString().match(/\d+(\.\d{1,3})?/g)[0];
                }
            },
//            {
//                data: 'perfPerDollar',
//                searchable: false,
//                "render": function(data, type, full) {
//                    return data.toString().match(/\d+(\.\d{1,3})?/g)[0];
//                }
//            },
//            {
//                data: 'perfPerWatt',
//                searchable: false,
//                "render": function(data, type, full) {
//                    return data.toString().match(/\d+(\.\d{1,3})?/g)[0];
//                }
//            },
            {
                data: 'coefficientOfVariation',
                searchable: false,
            },
            {
                data: 'runCount'
            },
            {
                data: 'runType'
            }
            ,
            {
                data: 'workload'
            }
        ],
    });

}

$('#appDrop').change(
    function() {
        $.getJSON("/bmsDashboard", {
            appName: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">--Benchmark--</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#bmDrop').html(html);
        });
    });


$('#cpuGenDrop').change(
    function() {
        $.getJSON("/cpusGen", {
            cpuGen: $(this).val(),
            ajax: 'true'
        }, function(data) {
            var html = '<option value="" selected="true" disabled="disabled">-- CPU --</option>';
            var len = data.length;
            for (var i = 0; i < len; i++) {
                html += '<option value="' + data[i] + '">' +
                    data[i] + '</option>';
            }
            html += '</option>';
            $('#cpuDrop').html(html);
        });
    });

$('#appDrop').change(function() {
    var filter = '';
    $('#appDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(0).search(filter).draw();
});

$('select#bmDrop').change(function() {
    var filter = '';
    $('select#bmDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(1).search(filter).draw();
});

$('select#nodeDrop').change(function() {
    var filter = '';
    $('select#nodeDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(2).search(filter).draw();
});

$('select#cpuDrop').change(function() {
    var filter = '';
    $('select#cpuDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(3).search(filter).draw();
});


$('select#runTypeDrop').change(function() {
    var filter = '';
    $('select#runTypeDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });

    filter = filter.substring(0, filter.length - 1);
    table.column(9).search(filter).draw();
});

$('select#runCountDrop').change(function() {
    var filter = '';
    $('select#runCountDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });

    filter = filter.substring(0, filter.length - 1);
    table.column(8).search(filter).draw();
});

$('select#workloadDrop').change(function() {
    var filter = '';
    $('select#workloadDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });

    filter = filter.substring(0, filter.length - 1);
    table.column(11).search(filter).draw();
});

$('#clearButton').on('click', function() {

    $('select').prop('selectedIndex', 0);
    dataTable();
});