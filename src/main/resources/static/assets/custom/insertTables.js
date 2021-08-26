getData();


function getData() {


    $.getJSON("/helper/appMetricStatus/", function(data) {

        var columnNames = ['appName', 'metric', 'status'];
        updateTableStatus(columnNames, data);
    });



    $.getJSON("/helper/procStatus/", function(data) {

        var columnNames = ['cpuGeneration', 'cpuSku', 'cores', 'price', 'tdp'];
        updateTableProc(columnNames, data);
    });

  $.getJSON("/helper/appCategory/", function(data) {

        var columnNames = ['segment', 'isv',  'appName', 'bmName', 'category'];
        updateTableCategory(columnNames, data);
    });

    $.getJSON("/helper/userList/", function(data) {

            var columnNames = ['First Name', 'Last Name',  'Email', 'Roles'];
            updateTableUsers(columnNames, data);
        });

}

function updateTableUsers(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-bordered ' id='fixTableHead'>" + getHeaders(columns) + getBody(columns, data) + "</table>";

    } else {
        table = "<p></p>";
    }

    $('#tableUsers').html(table);
}

function updateTableStatus(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-bordered ' id='fixTableHead'>" + getHeaders(columns) + getBody(columns, data) + "</table>";

    } else {
        table = "<p></p>";
    }

    $('#tableStatus').html(table);
}


function updateTableProc(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-bordered ' id='fixTableHead'>" + getHeaders(columns) + getBody(columns, data) + "</table>";

    } else {
        table = "<p></p>";
    }

    $('#tableProc').html(table);
}


function updateTableCategory(columns, data) {
    var table;

    if (data.length > 0) {
        table = "<table class='table table-bordered '  id='fixTableHead'>" + getHeaders(columns) + getBody(columns, data) + "</table>";

    } else {
        table = "<p></p>";
    }

    $('#tableCategory').html(table);
}


function getHeaders(columns) {
    var headers = ['<thead><tr>'];
    columns.forEach(function(column) {

        if (column == "appName") {
            headers.push('<th bgcolor="#D3D3D3">   App Name  </th>')
        }
        else if(column == "isv"){
        headers.push('<th bgcolor="#D3D3D3">  ISV  </th>')
        }
        else if(column == "bmName"){
        headers.push('<th bgcolor="#D3D3D3">  Benchmark  </th>')
                }
        else if(column == "price"){
         headers.push('<th bgcolor="#D3D3D3">  Price ($)  </th>')
          }
         else if(column == "tdp"){
         headers.push('<th bgcolor="#D3D3D3">  TDP (W)  </th>')
                    }
        else {
            headers.push('<th bgcolor="#D3D3D3">' + column.charAt(0).toUpperCase() + column.slice(1) + '</th>')
        }
    });
    headers.push('</tr></thead>');

    return headers.join('');
}

function getBody(columns, data) {
    var body = ['<tbody>'];
    data.forEach(function(row) {
        body.push(generateRow(columns, row))
    });
    body.push('</tbody>');

    return body.join('');
}


function generateRow(columns, rowData) {
    var row = ['<tr>'];
    var val;

    columns.forEach(function(column) {
        if (rowData[column] != undefined) {
            val = rowData[column];

        } else {
            val = '';
        }

        row.push('<td>' + val + '</td>')

    });

    row.push('</tr>');

    return row.join('');
}