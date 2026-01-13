INSERT INTO tb_email (
    email_id, user_id, email_from, email_to, email_subject, body, send_date_email, status_email
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    '22222222-2222-2222-2222-222222222222',
    'exemplo@dominio.com',
    'destino@dominio.com',
    'Assunto de Exemplo',
    'Corpo do email de exemplo.',
    NOW(),
    'SENT'
);