let scanner;
let scannedCode = "";

/* ---------- UI TOGGLE ---------- */
function showScan() {
    document.getElementById("scanSection").classList.remove("hidden");
    document.getElementById("manualSection").classList.add("hidden");
    startCamera();
}

function showManual() {
    stopCamera();
    document.getElementById("manualSection").classList.remove("hidden");
    document.getElementById("scanSection").classList.add("hidden");
}

/* ---------- CAMERA ---------- */
function startCamera() {
    document.getElementById("reader").style.display = "block";
    scanner = new ZXing.BrowserMultiFormatReader();

    const video = document.createElement("video");
    document.getElementById("reader").innerHTML = "";
    document.getElementById("reader").appendChild(video);

    scanner.decodeFromVideoDevice(null, video, (result, err) => {
        if (result) {
            scannedCode = result.text;
            document.getElementById("message").innerHTML =
                `<p style="color:green">Scanned: ${scannedCode}</p>`;
            stopCamera();
        }
    });
}

function stopCamera() {
    if (scanner) scanner.reset();
}

/* ---------- ADD SCANNED MED ---------- */
function addScannedMed() {
    const qty = document.getElementById("scanQty").value;

    if (!scannedCode || qty <= 0) {
        alert("Scan barcode and enter quantity");
        return;
    }

    fetch(`http://localhost:8080/api/medicine/add?barcode=${scannedCode}&quantity=${qty}`, {
        method: "POST"
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(t => { throw new Error(t); });
        }
        return res.json();
    })
    .then(data => {
        document.getElementById("message").innerHTML =
            `<p style="color:green">✅ ${data.name} added successfully</p>`;
        document.getElementById("scanQty").value = "";
    })
    .catch(err => {
        document.getElementById("message").innerHTML =
            `<p style="color:red">❌ ${err.message}</p>`;
    });
}

/* ---------- ADD MANUAL MEDICINE ---------- */
function addManualMed() {

    const med = {
        name: document.getElementById("name").value,
        batchNo: document.getElementById("batch").value,
        barcode: document.getElementById("barcode").value,
        expiryDate: document.getElementById("expiry").value,
        manufacturer: document.getElementById("manufacturer").value,
        category: document.getElementById("category").value,
        quantity: parseInt(document.getElementById("quantity").value)
    };

    if (!med.name || !med.batchNo || !med.expiryDate || !med.quantity) {
        alert("Please fill all required fields");
        return;
    }

    fetch("http://localhost:8080/api/medicine/manual", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(med)
    })
    .then(res => {
        if (!res.ok) {
            return res.text().then(t => { throw new Error(t); });
        }
        return res.json();
    })
    .then(data => {
        document.getElementById("message").innerHTML =
            `<p style="color:green">✅ ${data.name} saved successfully</p>`;

        // clear fields
        document.querySelectorAll("#manualSection input")
            .forEach(i => i.value = "");
    })
    .catch(err => {
        document.getElementById("message").innerHTML =
            `<p style="color:red">❌ ${err.message}</p>`;
    });
}




