# TestWorkManager

### Projeto para testar workmanager
<img width="499" alt="image" src="https://user-images.githubusercontent.com/11971209/229808799-5150619e-cbee-48dd-83f7-2290b7f04850.png">

**Iniciar serviço** Apenas inicia o serviço, guarda o log `ServiceRunner.onStartCommand entry`

**Agendar serviço** Agenda o serviço conforme as configurações, guarda os logs: `ServiceSheduler.onStartCommand entry` e o `ServiceScheduler.createPeriodicWorkRequest <interval>`

### Configurações
<img width="496" alt="image" src="https://user-images.githubusercontent.com/11971209/229809864-1339ed76-4424-4e17-a2bc-085237e8b3eb.png">
A tela de configuração são os parametros usados no Workmanager:
<img width="617" alt="image" src="https://user-images.githubusercontent.com/11971209/229810754-e4361bdc-0c53-4e6b-a07e-5b983e2c58f5.png">

os campos `Success`, `Failure` e `Retry` estao associado ao peso de cada possibilidade(exemplo: colocando respectivamente 1, 10 e 22; significa que tem 1/33 de chance do job ter sucesso, 10/33 de chance do job terminar com falha e 22/33 do job terminar com comando de retry)
