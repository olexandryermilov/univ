let DATABASES = [];
let TABLES = [];
let TABLE = [];
const MSG = $('#message');
const DB_TABLE = $('#db-table');
const DB_NAV = $('#databases-nav');
const TABLES_NAV = $('#tables-nav');

/*function update_tables() {
    if (CURRENT_DB_INDEX === -1) {
        TABLES_NAV.css('max-height', '0px');
        TABLES = [];
        TABLES_NAV.html('<div class="first">Tables (E):</div>');
        return;
    }
    $.get('/databases/' + DATABASES[CURRENT_DB_INDEX] + '/tables', function (response) {
        if (response.status === 'OK') {
            TABLES = response['result'];
            TABLES_NAV.css('max-height', '60px');
            TABLES_NAV.html('<div class="first">Tables:</div>');
            for (let i = 0; i < TABLES.length; i++) {
                TABLES_NAV.append('<div onClick="select_table(' + i + ')" id="table_' + i + '">' + TABLES[i] + '</div>');
            }
            TABLES_NAV.append('<div class="last">Create</div>');
        }
    });
}

function update_databases() {
    $.get('/databases', function (response) {
        if (response.status === 'OK') {
            DATABASES = response['result'];
            DB_NAV.html('<div class="first">Databases:</div>');
            for (let i = 0; i < DATABASES.length; i++) {
                DB_NAV.append('<div onClick="select_db(' + i + ')" id="db_' + i + '">' + DATABASES[i] + '</div>');
            }
            DB_NAV.append('<div class="last">Create</div>');
        }
    });
}

function select_db(db_index) {
    if (CURRENT_DB_INDEX !== db_index) {
        if (CURRENT_DB_INDEX !== -1) {
            $('#db_' + CURRENT_DB_INDEX).removeClass('active');
        }
        CURRENT_DB_INDEX = db_index;
        if (db_index !== -1) {
            update_tables();
            $('#db_' + db_index).addClass('active');
        }
    }
}

function select_table(table_index) {
    if (CURRENT_TABLE_INDEX !== table_index) {
        if (CURRENT_TABLE_INDEX !== -1) {
            $('#table_' + CURRENT_TABLE_INDEX).removeClass('active');
        }
        CURRENT_TABLE_INDEX = table_index;
        if (table_index !== -1) {
            update_table();
            $('#table_' + table_index).addClass('active');
        }
    }
}

function update_table() {
    if (CURRENT_TABLE_INDEX === -1) {
        MSG.show();
        return;
    }
    MSG.hide();
    $.get('/databases/' + DATABASES[CURRENT_DB_INDEX] + '/tables/' + TABLES[CURRENT_TABLE_INDEX], function (response) {
        if (response.status === 'OK') {
            TABLE = response['result'];
            DB_TABLE.html('');
            let row = $('<tr></tr>');
            for (let i = 0; i < TABLE.columns.length; i++) {
                let col = TABLE.columns[i];
                row.append($('<td>' + col.name + ':' + col.type + '</td>'));
            }
            row.append($('<td></td>'));
            DB_TABLE.append(row);
            for (let i = 0; i < TABLE.data.length; i++) {
                let row = $('<tr></tr>');
                for (let j = 0; j < TABLE.data[i].length; j++) {
                    row.append($('<td>' + TABLE.data[i][j] + '</td>'));
                }
                row.append($('<td>-</td>'));
                DB_TABLE.append(row);
            }
            row = $('<tr></tr>');
            for (let i = 0; i < TABLE.columns.length; i++) {
                row.append($('<td>value</td>'));
            }
            row.append($('<td>+</td>'));
            DB_TABLE.append(row);
        }
    });
}

update_databases();
update_tables();
update_table();*/
function getAllDatabases() {
    $.get()
}