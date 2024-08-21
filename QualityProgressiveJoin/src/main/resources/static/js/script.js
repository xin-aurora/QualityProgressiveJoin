function sendData() {
    let SQLSelect = document.getElementById("SQLSelect").value;
    let SQLFrom = document.getElementById("SQLFrom").value;
    let SQLWhere = document.getElementById("SQLWhere").value;
    let SQLGroupBy = document.getElementById("SQLGroupBy").value;
    // Create a URLSearchParams object to hold the data
    let params = new URLSearchParams();
    params.append('SQLSelect', SQLSelect);
    params.append('SQLFrom', SQLFrom);
    params.append('SQLWhere', SQLWhere);
    params.append('SQLGroupBy', SQLGroupBy);

    fetch('/process', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(result => {
        // Fill the results in the table
        let resultsTable = document.getElementById("resultsTable");
        let newRow = resultsTable.insertRow();
        let cell1 = newRow.insertCell(0);
        let cell2 = newRow.insertCell(1);
        cell1.innerHTML = result.input;
        cell2.innerHTML = result.output;
    })
    .catch(error => console.error('Error:', error));
}