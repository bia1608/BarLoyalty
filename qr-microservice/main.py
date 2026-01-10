from fastapi import FastAPI, HTTPException
import uuid
import requests

app = FastAPI()

pending_transactions = {}

@app.post("/qr/generate")
async def generate_qr_code(transaction_details: dict):
    transaction_id = str(uuid.uuid4())
    pending_transactions[transaction_id] = transaction_details
    print(f"PYTHON: Generated QR ID: {transaction_id}")
    return {"qr_code_id": transaction_id}

@app.post("/qr/validate/{transaction_id}")
async def validate_qr_code(transaction_id: str):
    if transaction_id not in pending_transactions:
        print(f"PYTHON ERROR: QR ID {transaction_id} not found.")
        raise HTTPException(status_code=404, detail="Cod QR invalid sau expirat")

    transaction_details = pending_transactions[transaction_id]
    print(f"PYTHON: Validating QR ID: {transaction_id}")

    try:
        gateway_url = "http://gateway:8080/api/transactions/confirm"
        # Adaugam autentificare Basic. Python va trimite user/parola la Java.
        response = requests.post(
            gateway_url,
            json=transaction_details,
            auth=('qr-service-user', 'secret-password')
        )
        response.raise_for_status()

        del pending_transactions[transaction_id]
        print("PYTHON: Gateway confirmed transaction.")
        return {"status": "success", "message": "Tranzactie validata si confirmata"}

    except requests.exceptions.RequestException as e:
        print(f"PYTHON ERROR: Could not connect to Gateway: {e}")
        raise HTTPException(status_code=503, detail="Serviciul Gateway nu este disponibil")