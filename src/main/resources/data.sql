DELETE FROM public.papel_servico;
DELETE FROM public.usuario_papel;
DELETE FROM public.usuario;
DELETE FROM public.papel;
DELETE FROM public.servico;

INSERT INTO public.usuario (usuario_id, nome_de_usuario, senha) VALUES(1, 'admin', '21232f297a57a5a743894a0e4a801fc3');
INSERT INTO public.usuario (usuario_id, nome_de_usuario, senha) VALUES(2, 'funcionario', 'cc7a84634199040d54376793842fe035');

INSERT INTO public.papel (papel_id, nome) VALUES(1, 'ADMIN');
INSERT INTO public.papel (papel_id, nome) VALUES(2, 'FUNCIONARIO');

INSERT INTO public.usuario_papel (usuario_id, papel_id) VALUES(1, 1);
INSERT INTO public.usuario_papel (usuario_id, papel_id) VALUES(1, 2);
INSERT INTO public.usuario_papel (usuario_id, papel_id) VALUES(2, 2);

INSERT INTO public.servico (servico_id, uri) VALUES(1, '/banco/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(2, '/banco/list');
INSERT INTO public.servico (servico_id, uri) VALUES(3, '/banco/save');
INSERT INTO public.servico (servico_id, uri) VALUES(4, '/banco/show');
INSERT INTO public.servico (servico_id, uri) VALUES(5, '/banco/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 1);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 2);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 3);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 4);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 5);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 2);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 4);

INSERT INTO public.servico (servico_id, uri) VALUES(6, '/estado/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(7, '/estado/list');
INSERT INTO public.servico (servico_id, uri) VALUES(8, '/estado/save');
INSERT INTO public.servico (servico_id, uri) VALUES(9, '/estado/show');
INSERT INTO public.servico (servico_id, uri) VALUES(10, '/estado/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 6);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 7);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 8);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 9);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 10);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 7);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 9);

INSERT INTO public.servico (servico_id, uri) VALUES(11, '/agencia/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(12, '/agencia/list');
INSERT INTO public.servico (servico_id, uri) VALUES(13, '/agencia/save');
INSERT INTO public.servico (servico_id, uri) VALUES(14, '/agencia/show');
INSERT INTO public.servico (servico_id, uri) VALUES(15, '/agencia/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 11);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 12);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 13);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 14);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 15);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 12);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 14);

INSERT INTO public.servico (servico_id, uri) VALUES(16, '/cidade/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(17, '/cidade/list');
INSERT INTO public.servico (servico_id, uri) VALUES(18, '/cidade/save');
INSERT INTO public.servico (servico_id, uri) VALUES(19, '/cidade/show');
INSERT INTO public.servico (servico_id, uri) VALUES(20, '/cidade/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 16);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 17);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 18);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 19);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 20);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 17);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 19);

INSERT INTO public.servico (servico_id, uri) VALUES(21, '/clientePessoaFisica/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(22, '/clientePessoaFisica/list');
INSERT INTO public.servico (servico_id, uri) VALUES(23, '/clientePessoaFisica/save');
INSERT INTO public.servico (servico_id, uri) VALUES(24, '/clientePessoaFisica/show');
INSERT INTO public.servico (servico_id, uri) VALUES(25, '/clientePessoaFisica/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 21);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 22);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 23);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 24);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 25);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 22);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 24);

INSERT INTO public.servico (servico_id, uri) VALUES(26, '/clientePessoaJuridica/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(27, '/clientePessoaJuridica/list');
INSERT INTO public.servico (servico_id, uri) VALUES(28, '/clientePessoaJuridica/save');
INSERT INTO public.servico (servico_id, uri) VALUES(29, '/clientePessoaJuridica/show');
INSERT INTO public.servico (servico_id, uri) VALUES(30, '/clientePessoaJuridica/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 26);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 27);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 28);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 29);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 30);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 27);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 29);

INSERT INTO public.servico (servico_id, uri) VALUES(31, '/contaCorrente/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(32, '/contaCorrente/list');
INSERT INTO public.servico (servico_id, uri) VALUES(33, '/contaCorrente/save');
INSERT INTO public.servico (servico_id, uri) VALUES(34, '/contaCorrente/show');
INSERT INTO public.servico (servico_id, uri) VALUES(35, '/contaCorrente/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 31);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 32);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 33);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 34);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 35);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 32);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 34);

INSERT INTO public.servico (servico_id, uri) VALUES(36, '/contaPoupanca/delete');
INSERT INTO public.servico (servico_id, uri) VALUES(37, '/contaPoupanca/list');
INSERT INTO public.servico (servico_id, uri) VALUES(38, '/contaPoupanca/save');
INSERT INTO public.servico (servico_id, uri) VALUES(39, '/contaPoupanca/show');
INSERT INTO public.servico (servico_id, uri) VALUES(40, '/contaPoupanca/update');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 36);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 37);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 38);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 39);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 40);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 37);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 39);

INSERT INTO public.servico (servico_id, uri) VALUES(41, '/correntista/associate');
INSERT INTO public.servico (servico_id, uri) VALUES(42, '/correntista/showByCliente"');
INSERT INTO public.servico (servico_id, uri) VALUES(43, '/correntista/showByConta');

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 41);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 42);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(1, 43);

INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 42);
INSERT INTO public.papel_servico (papel_id, servico_id) VALUES(2, 43);