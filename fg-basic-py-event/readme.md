.\run.bat
py -m pip install -r requirements.txt

py create_zip.py

{
    "queryStringParameters": {
        "name": "Alejandro"
    }
}

py server_basic.py --port 8080 
http://localhost:8080/?name=Alejandro
https://3606a742f89644148f83092cf970121d.apic.la-south-2.huaweicloudapis.com/fg-basic-py/hello?name=Alejandro

py test_local.py