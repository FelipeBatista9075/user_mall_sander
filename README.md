# 📧 user_mail_sender

Microserviço responsável pelo **envio de e-mails** de forma **assíncrona**, utilizando **RabbitMQ** como broker de mensagens e **SMTP** para o disparo dos e-mails.

Esse serviço faz parte de uma arquitetura de **microserviços**, desacoplando a responsabilidade de envio de e-mail de outros domínios da aplicação.

---

## 🚀 Tecnologias Utilizadas

* ☕ **Java 17**
* 🌱 **Spring Boot**
* 🐰 **RabbitMQ** (mensageria)
* 📬 **SMTP** (envio de e-mails)
* 🐳 **Docker & Docker Compose**
* 🧪 **JUnit / Mockito**
* 📦 **Maven**

---

## 🧠 Visão Geral da Arquitetura

```text
[ User Service ]
      │
      │  (Producer envia EmailDto)
      ▼
 RabbitMQ (Exchange ➜ Queue)
      │
      │  (Consumer escuta a fila)
      ▼
 user_mail_sender
      │
      ▼
   SMTP Server
```

* O **User Service** produz eventos de envio de e-mail
* O **RabbitMQ** atua como intermediário desacoplado
* O **user_mail_sender** consome a mensagem e envia o e-mail via SMTP

````

- O serviço **consome mensagens** de uma fila RabbitMQ
- Cada mensagem contém os dados necessários para o envio do e-mail
- O envio é realizado via **SMTP**, garantindo desacoplamento e escalabilidade

---

## 📂 Estrutura de Pastas

```bash
src/main/java/dev/java10x/email
├── config        # Configurações (RabbitMQ, SMTP, Beans)
├── consumer      # Consumers RabbitMQ
├── dto           # Objetos de transferência de dados
├── entities      # Entidades de domínio
├── repository    # Acesso a dados (se aplicável)
├── service       # Regras de negócio e envio de e-mail
└── EmailApplication.java
````

---

## 🐰 RabbitMQ

O microserviço utiliza o RabbitMQ para **processamento assíncrono** de envio de e-mails.

### 📤 Producer (User Service)

Responsável por publicar mensagens quando um evento ocorre (ex: cadastro de usuário).

```java
public void sendProducerMessage(UserModel model) {
    EmailDto emailDto = new EmailDto();
    emailDto.setUserId(model.getUserId());
    emailDto.setEmailTo(model.getEmail());
    emailDto.setEmailSubject("Welcome " + model.getName() + " to Our Service");
    emailDto.setBody(
        "Your registration is successful!
" +
        "We're excited to have you on board.
" +
        "If you have any questions, feel free to reach out to our support team.
" +
        "Best regards, The Team"
    );

    rabbitTemplate.convertAndSend(
        EMAIL_EXCHANGE,
        EMAIL_ROUTING_KEY,
        emailDto
    );
}
```

---

### 📥 Consumer (user_mail_sender)

Responsável por consumir mensagens da fila e realizar o envio do e-mail.

```java
@RabbitListener(queues = "email-queue")
public void listenEmailQueue(@Payload EmailDto dto) {
    var emailModel = new EmailModel();
    BeanUtils.copyProperties(dto, emailModel);
    emailService.sendEmail(emailModel);
}
```

### 🔁 Funcionamento

* O `EmailDto` é enviado para a exchange
* O RabbitMQ roteia para a fila `email-queue`
* O listener consome a mensagem automaticamente
* O serviço SMTP é acionado

json
{
"to": "[user@email.com](mailto:user@email.com)",
"subject": "Bem-vindo!",
"body": "Seu cadastro foi realizado com sucesso."
}

````

---

## 📬 Envio de E-mail (SMTP)

- Suporte a SMTP com autenticação
- Compatível com serviços como:
  - Gmail

As configurações ficam no arquivo:

```properties
application.yml / application.properties
````

---

## 🐳 Docker

O projeto possui suporte a **Docker Compose** para facilitar a execução do RabbitMQ e do serviço.

```bash
docker-compose up -d
```

---

## ▶️ Como Executar Localmente

1. Suba o RabbitMQ (Docker ou local)
2. Configure as variáveis SMTP
3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

---


## ✨ Futuras Melhorias

* Retry com Dead Letter Queue (DLQ)
* Templates de e-mail (Thymeleaf / Freemarker)
* Suporte a anexos
* Observabilidade (Logs, Metrics, Tracing)

---

## 👨‍💻 Autor

Felipe Batista

---

📌 *Microserviço simples, desacoplado e escalável para envio de e-mails em arquiteturas modernas.*
