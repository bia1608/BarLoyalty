from fastapi import FastAPI

app = FastAPI()

@get("/")
def read_root():
    return {"Hello": "from QR Service"}