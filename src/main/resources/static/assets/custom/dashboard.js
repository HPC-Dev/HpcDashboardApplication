dataTable();
var table;
function dataTable() {

var buttonCommon = {
        exportOptions: {
            format: {
                body: function ( data, row, column, node ) {
                    return data;
                }
            }
        }
        };

table = $('table#ajax').DataTable({
    'ajax': '/datatable/dashboard',
    'scrollY': "550px",
    'scrollX': true,
    'destroy': true,
    'scrollCollapse': true,
    'processing': true,
    'serverSide': true,
    "dom": 'lBfrtip',
    'lengthMenu': [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
    'buttons': [
                {
                    extend: 'collection',
                    text: 'Export',
                    buttons: [
                        'copy',
                        'excel'
                        ,'csv']
                }
            ],

    columns: [{
            data: 'jobId'
        },
        {
            data: 'appName'
        },
        {
            data: 'bmName'
        },
        {
            data: 'nodes'
        },
        {
            data: 'cores'
        },
        {
            data: 'nodeName'
        },
        {
            data: 'result'
        },
        {
            data: 'cpu'
        },
        {
            data: 'os'
        },
        {
            data: 'biosVer'
        },
        {
            data: 'cluster'
        },
        {
            data: 'user'
        },
        {
            data: 'platform'
        },
        {
            data: 'cpuGen'
        },
        {
            data: 'runType'
        },
        {
        data: 'timeStamp',
        searchable: false,
        "render": function (data) {
                if(data != null){
                 var dateData = data.split(' ');
                 var date = dateData[0].split('-');
                return (date[1] + "/" + date[0] + "/" + date[2]);
                }
                else{
                return null;
                }
            }
        },
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
    table.column(1).search(filter).draw();
});

$('select#bmDrop').change(function() {
    var filter = '';
    $('select#bmDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(2).search(filter).draw();
});

$('select#nodeDrop').change(function() {
    var filter = '';
    $('select#nodeDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(3).search(filter).draw();
});

$('select#cpuDrop').change(function() {
    var filter = '';
    $('select#cpuDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(7).search(filter).draw();
});

$('select#osDrop').change(function() {
    var filter = '';
    $('select#osDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(8).search(filter).draw();
});


$('select#biosDrop').change(function() {
    var filter = '';
    $('select#biosDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(9).search(filter).draw();
});

$('select#clusterDrop').change(function() {
    var filter = '';
    $('select#clusterDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(10).search(filter).draw();
});

$('select#userDrop').change(function() {
    var filter = '';
    $('select#userDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(11).search(filter).draw();
});

$('select#platformDrop').change(function() {
    var filter = '';
    $('select#platformDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(12).search(filter).draw();
});

$('select#cpuGenDrop').change(function() {
    var filter = '';
    $('select#cpuGenDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });
    filter = filter.substring(0, filter.length - 1);
    table.column(13).search(filter).draw();
});

$('select#runTypeDrop').change(function() {
    var filter = '';
    $('select#runTypeDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });

    filter = filter.substring(0, filter.length - 1);
    table.column(14).search(filter).draw();
});

$('#startDate').change(addDateFilter);
$('#endDate').change(addDateFilter);

function addDateFilter() {
     var startDate = $('#startDate').val();
     var endDate = $('#endDate').val();
     table.column(15).search(startDate + ',' + endDate).draw();
}


$('select#workloadDrop').change(function() {
    var filter = '';
    $('select#workloadDrop option:selected').each(function() {
        filter += $(this).text() + "+";
    });

    filter = filter.substring(0, filter.length - 1);
    table.column(16).search(filter).draw();
});

//$('#showButton').on('click', function(){
//        $('#ajax').DataTable().destroy();
//        $('#ajax tbody').empty();
//});

$('#clearButton').on('click', function(){

$('select').prop('selectedIndex',0);
$('#startDate').val('');
$('#endDate').val('');
dataTable();
});
