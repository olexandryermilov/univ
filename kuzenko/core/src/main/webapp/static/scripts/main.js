let DATABASES = [];
let TABLES = [];
let TABLE = [];
let COLUMNS = [];
let KEY_INDEX = undefined;
let KEY = undefined;
let SECOND_TABLE_INDEX = undefined;
const DB_TABLE = $('#db-table');
const DB_NAV = $('#databases-nav');
const TABLES_NAV = $('#tables-nav');
const TABLES_TO_MERGE_NAV = $('#tables-to-merge-nav');

function wrapValue(stringValue) {
    return '\'' + stringValue + '\''
}

function getDatabase(databaseName) {
    $.ajax({
        url: "http://localhost:8080/database/" + databaseName
    }).then(function (data, status, jqxhr) {
        console.log(data);
        CURRENT_DB = data;
        TABLES = data.tables;
        TABLES_NAV.html('<div class="first">Tables:</div>');
        for (let i = 0; i < TABLES.length; i++) {
            console.log(TABLES[i]);
            var tableName = TABLES[i].tableName;
            TABLES_NAV.append('<div onclick="getTable(' + wrapValue(tableName) + ', ' + wrapValue(databaseName) + ')">' + TABLES[i].tableName + '</div>');
        }
        TABLES_NAV.append('<div><input type = "text" id="table_input"/></div>');
        TABLES_NAV.append('<div class="last" id="createTable" onclick="prepareCreatingTable()">Create</div>');
        TABLES_NAV.append('<div class="last" id="deleteTable" onclick="deleteTable()">Delete</div>');
        TABLES_NAV.append('<div class="last" id="deleteTable" onclick="prepareMergeTables()">Merge</div>');
    });
}

function getTable(tableName, databaseName) {
    $.ajax({
        url: "http://localhost:8080/database/" + databaseName + "/table/" + tableName
    }).then(function (data, status) {
        console.log(data);
        TABLE = data;
        KEY = data.key;
        COLUMNS = data.columns;
        DB_TABLE.html('');
        TABLES_TO_MERGE_NAV.html('');
        let row = $('<tr></tr>');
        for (let i = 0; i < TABLE.columns.length; i++) {
            let col = TABLE.columns[i];
            row.append($('<td>' + col.columnName + '(' + col.columnType + ')</td>'));
            if (col.columnName === KEY) {
                KEY_INDEX = i;
            }
        }
        row.append($('<td></td>'));
        DB_TABLE.append(row);
        for (let i = 0; i < TABLE.rows.length; i++) {
            let row = $('<tr></tr>');
            for (let j = 0; j < COLUMNS.length; j++) {
                var value = undefined;
                if (j < TABLE.rows[i].values.length) value = TABLE.rows[i].values[j];
                else value = " ";
                row.append($('<td>' + value + '</td>'));
            }
            row.append($('<td onclick="deleteRow(' + wrapValue(TABLE.rows[i].values[KEY_INDEX]) + ',' + wrapValue(databaseName) + ')">-</td>'));
            DB_TABLE.append(row);
        }
        row = $('<tr></tr>');
        for (let i = 0; i < TABLE.columns.length; i++) {
            row.append($('<td><input type ="text" id = "column_(' + wrapValue(COLUMNS[i].columnName) + ')"></td>'));
        }
        row.append($('<td onclick="addRow()">+</td>'));
        DB_TABLE.append(row);
    });
}

function getAllDatabases() {
    $(document).ready(function () {
        $.ajax({
            url: "http://localhost:8080/database/"
        }).then(function (data, status, jqxhr) {
            console.log(data);
            DATABASES = data;
            console.log(DATABASES);
            DB_NAV.html('<div class="first">Databases:</div>');
            for (let i = 0; i < DATABASES.length; i++) {
                var dbName = DATABASES[i].databaseName;
                DB_NAV.append('<div onclick="getDatabase(\'' + dbName + '\')" id="db_' + i + '">' + dbName + '</div>');
            }
            DB_NAV.append('<div><input type = "text" id="db_input"/></div>');
            DB_NAV.append('<div class="last" onclick="createDatabase()">Create</div>');
            DB_NAV.append('<div class="last" onclick="deleteDatabase()">Delete</div>');
        });
    });
}

function deleteRow(keyValue, databaseName) {
    $.ajax({
        url: "http://localhost:8080/database/" + databaseName + "/table/" + TABLE.tableName + "/row/" + keyValue,
        type: 'DELETE'
    }).then(function (data, status, jqxhr) {
        console.log(data);
        console.log(status);
        console.log(jqxhr);
        getTable(TABLE.tableName, databaseName)
    })
}

function createDatabase() {
    $.ajax({
        url: "http://localhost:8080/database/",
        type: 'POST',
        data: JSON.stringify({"databaseName": db_input.value}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).then(function (data, status, jqxhr) {
        console.log(data);
        console.log(status);
        getAllDatabases();
    });
}

function deleteDatabase() {
    $.ajax({
        url: "http://localhost:8080/database/" + CURRENT_DB.databaseName,
        type: 'DELETE',
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).then(function (data, status, jqxhr) {
        CURRENT_DB = undefined;
        console.log(status);
    });
    getAllDatabases();
}

function deleteTable() {
    $.ajax({
        url: "http://localhost:8080/database/" + CURRENT_DB.databaseName + "/table/" + TABLE.tableName,
        type: 'DELETE',
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).then(function (data, status, jqxhr) {
        console.log(TABLE.tableName);
        TABLE = undefined;
        console.log(status);
        getDatabase(CURRENT_DB.databaseName);
        DB_TABLE.html('')
    });
    getDatabase(CURRENT_DB.databaseName);
}

function createTable(tableName) {
    $.ajax({
        url: "http://localhost:8080/database/" + CURRENT_DB.databaseName + "/table",
        type: 'POST',
        data: JSON.stringify({
            "tableName": tableName,
            "key": NEW_COLUMNS[0].columnName,
            "columns": NEW_COLUMNS
        }),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).then(function (data, status, jqxhr) {
        console.log(data);
        console.log(status);
        getDatabase(CURRENT_DB.databaseName);
        DB_TABLE.html('');
    });
}

function prepareCreatingTable() {
    TABLE = undefined;
    KEY = undefined;
    COLUMNS = undefined;
    KEY_INDEX = undefined;
    NEW_COLUMNS = [];
    DB_TABLE.html('');
    var tableName = table_input.value;
    let row = $('<tr></tr>');
    row = addColumnButton(row, tableName);
    DB_TABLE.append(row);
}

function addColumnButton(row, tableName) {
    row.append($('<td><input type = "text" id="columnName"/></td>'));
    var select = '<form><select id="columnType">';
    TYPES = ['Integer', 'Real', 'String', 'CharInv', 'Char'];
    for (var i = 0; i < TYPES.length; i++) {
        select = select + '<option value ="' + TYPES[i] + '">' + TYPES[i] + '</option>';
    }
    select = select + '</select></form>';
    row.append($('<td>' + select + '</td>'));
    console.log(select);
    row.append($('<td><button onclick="addColumn(' + wrapValue(tableName) + ')">Add column</button></td>'));
    return row;
}

function saveTableButton(row, tableName) {
    row.append($('<td><button onclick="createTable(' + wrapValue(tableName) + ')">Save table</button></td>'));
    return row;
}

function addColumn(tableName) {
    var column = columnName.value;
    var columnT = columnType.value;
    NEW_COLUMNS.push({
        "columnName": column,
        "columnType": columnT
    });
    console.log(COLUMNS);
    redrawColumnsForCreateTable(tableName);
}

function redrawColumnsForCreateTable(tableName) {
    let row = $('<tr></tr>');
    for (var i = 0; i < NEW_COLUMNS.length; i++) {
        row.append($('<td>' + NEW_COLUMNS[i].columnName + ' (' + NEW_COLUMNS[i].columnType + ')' + '</td>'));
    }
    row = addColumnButton(row, tableName);
    row = saveTableButton(row, tableName);
    DB_TABLE.html('');
    DB_TABLE.append(row);
}

function addRow() {
    for (var i = 0; i < TABLE.columns.length; i++) {

    }
}

function prepareMergeTables() {
    TABLES_TO_MERGE_NAV.html('<div class="first">Tables:</div>');
    for (let i = 0; i < TABLES.length; i++) {
        if (TABLES[i].tableName !== TABLE.tableName) {
            console.log(TABLES[i]);
            var table = TABLES[i];
            TABLES_TO_MERGE_NAV.append('<div onclick="mergeTablesStage2(' + i + ')">' + table.tableName + '</div>');
        }
    }

}

function mergeTablesStage2(secondTableIndex) {
    let secondTable = TABLES[secondTableIndex];
    console.log(secondTable.columns.toString());
    var firstTableColumnSet = new Set(TABLE.columns.map(x=>x.columnName));
    const secondTableColumnSet = new Set(secondTable.columns.map(x=>x.columnName));
    console.log(firstTableColumnSet);
    console.log(secondTableColumnSet);
    let newSet = [];
    for (let item of firstTableColumnSet) {
        if (secondTableColumnSet.has(item)) {
            newSet.push(item);
        }
    }
    console.log(newSet);
    var select = '<form><select id="joinOn">';
    for (var i = 0; i < newSet.length; i++) {
        select = select + '<option value ="' + newSet[i] + '">' + newSet[i] + '</option>';
    }
    select = select + '</select></form>';
    TABLES_TO_MERGE_NAV.append($('<td>' + select + '</td>'));
    TABLES_TO_MERGE_NAV.append('<div class="last" id="deleteTable" onclick="finallyMergeTables('+wrapValue(secondTable.tableName)+')">Merge</div>');
}

function finallyMergeTables(secondTableName){
    $.ajax({
        url: "http://localhost:8080/database/" + CURRENT_DB.databaseName + "/table/" + TABLE.tableName + "/merge/" + secondTableName,
        type: 'GET',
        data: {
            "joinOn": joinOn.value
        },
    }).then(function (data) {
        getDatabase(CURRENT_DB.databaseName);
        DB_TABLE.html('');
    });
}

getAllDatabases();