function sendData() {
    let inputData = document.getElementById("inputData").value;
    fetch('/process', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
            'data': inputData
        })
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