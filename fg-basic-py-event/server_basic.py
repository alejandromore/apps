#!/usr/bin/env python3
"""
Servidor HTTP básico para FunctionGraph - Solo biblioteca estándar
Escucha en puerto 8080 y procesa requests como FunctionGraph
"""

import http.server
import socketserver
import json
import sys
import threading
from datetime import datetime
from urllib.parse import urlparse, parse_qs

# Agregar directorio actual
sys.path.append('.')
from index import handler

class FunctionGraphHTTPHandler(http.server.BaseHTTPRequestHandler):
    """Handler HTTP que simula FunctionGraph"""
    
    def do_GET(self):
        """Manejar GET requests"""
        self._handle_request('GET')
    
    def _handle_request(self, method):
        """Manejar cualquier request HTTP"""
        # Parsear URL
        parsed_url = urlparse(self.path)
        path = parsed_url.path
        query_params = parse_qs(parsed_url.query)
        
        # Convertir query params de lista a valores simples
        simple_params = {}
        for key, value in query_params.items():
            simple_params[key] = value[0] if len(value) == 1 else value
        
        # Leer body si existe
        content_length = self.headers.get('Content-Length', 0)
        body = ''
        if content_length:
            body = self.rfile.read(int(content_length)).decode('utf-8')
        
        # Construir evento en formato FunctionGraph
        event = {
            'httpMethod': method,
            'path': path,
            'headers': dict(self.headers),
            'queryStringParameters': simple_params,
            'body': body if body else None,
            'requestContext': {
                'httpMethod': method,
                'path': path,
                'requestId': f'req-{datetime.now().timestamp()}',
                'requestTime': datetime.now().isoformat()
            }
        }
        
        # Contexto mock
        class MockContext:
            def __init__(self):
                self.function_name = 'local-server'
                self.memory_limit_in_mb = 128
                self.request_id = event['requestContext']['requestId']
                self.region = 'local'
            
            def get_remaining_time_in_millis(self):
                return 10000  # 10 segundos
        
        context = MockContext()
        
        try:
            # Ejecutar handler de FunctionGraph
            response = handler(event, context)
            
            # Enviar respuesta
            self.send_response(response.get('statusCode', 200))
            
            # Headers
            for key, value in response.get('headers', {}).items():
                self.send_header(key, value)
            
            # Headers CORS
            self._send_cors_headers()
            
            self.end_headers()
            
            # Body
            body = response.get('body', '')
            if body:
                self.wfile.write(body.encode('utf-8'))
                
            # Log
            print(f"[{datetime.now().strftime('%H:%M:%S')}] {method} {path} - {response.get('statusCode', 200)}")
            
        except Exception as e:
            print(f"❌ Error procesando request: {e}")
            self.send_response(500)
            self._send_cors_headers()
            self.end_headers()
            self.wfile.write(json.dumps({'error': str(e)}).encode('utf-8'))
    
    def _send_cors_headers(self):
        """Agregar headers CORS"""
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
    
    def log_message(self, format, *args):
        """Sobrescribir log para personalizar"""
        # No hacer nada para silenciar logs por defecto
        pass

def start_server(port=8080):
    """Iniciar servidor HTTP"""
    with socketserver.TCPServer(("", port), FunctionGraphHTTPHandler) as httpd:
        print("=" * 60)
        print(f"🚀 SERVIDOR FUNCTIONGRAPH LOCAL INICIADO")
        print(f"📡 Puerto: {port}")
        print(f"🌐 URL: http://localhost:{port}")
        print(f"📅 Hora: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print("=" * 60)
        print("\n📋 Endpoints disponibles:")
        print(f"  GET  http://localhost:{port}")
        print("\n📝 Métodos soportados: GET")
        print("\n🛑 Presiona Ctrl+C para detener el servidor")
        print("=" * 60)
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\n\n👋 Servidor detenido por el usuario")
        finally:
            httpd.server_close()

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(description='Servidor HTTP local para FunctionGraph')
    parser.add_argument('--port', type=int, default=8080, help='Puerto a escuchar')
    parser.add_argument('--host', default='localhost', help='Host a bindear')
    
    args = parser.parse_args()
    
    start_server(args.port)