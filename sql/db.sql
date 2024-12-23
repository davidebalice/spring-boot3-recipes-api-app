

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";




CREATE TABLE `recipes_category` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `id_category` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



INSERT INTO `recipes_category` (`id`, `name`, `description`, `id_category`) VALUES
(1, 'Tv', 'lorem ipsum', 0),
(2, 'Smartphone', 'lorem ipsum', 0),
(3, 'Game', 'lorem ipsum', 0),
(4, 'Book', 'lorem ipsum', 0),
(5, 'Ebook', 'Lorem ipsum', 0);



CREATE TABLE `recipes_recipes` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `id_category` int(11) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `image_url` varchar(200) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;




INSERT INTO `recipes_recipes` (`id`, `title`, `id_category`, `description`) VALUES
(2, 'aaaaaaaa', 1, '123123123'),
(4, 'vvvvvvvvv', 1, '12312312312');


CREATE TABLE `recipes_ingredients` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `id_recipe` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


CREATE TABLE `recipes_shopping_list` (
  `id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


CREATE TABLE `recipes_user` (
  `id` int(11) NOT NULL,
  `name` varchar(200) DEFAULT NULL,
  `surname` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `role` varchar(100) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT 1,
  `birthdate` date DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `profile_image_url` varchar(200) DEFAULT NULL,
  `last_access` date DEFAULT NULL,
  `authentication_token` varchar(200) DEFAULT NULL,
  `password_reset_token` int(200) DEFAULT NULL,
  `login_attempts` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `recipes_user` (`id`, `name`, `surname`, `email`, `password`, `username`, `role`, `created_at`, `updated_at`, `active`, `birthdate`, `address`, `phone_number`, `profile_image_url`, `last_access`, `authentication_token`, `password_reset_token`, `login_attempts`) VALUES
(1, 'Mario', 'Rossi', 'mario@rossi.it', '$2a$10$LqjcSXTUekCnmlmrWaCb.elGv/VFm2FGQCfUEO.KcOHEbFQN5ZLEC', 'mariorossi', 'admin', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, '', 0, 0),
(2, 'Luigi', 'Bianchi', 'luigi@bianchi.it', '$2a$10$q7yZy7zta7ZyzQrLwUlvoOhTjZkcjtBZ0e4qtKKYeZzYrs76IwcYy', 'luiginetto13', 'admin', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, '', 0, 0),
(3, 'Ciccio', 'Neri', 'ciccio@neri.it', '$2a$10$YuKwf4hjc8Wa5y/uaYPYz.V/NMwxTD7YC4OSd9ODO1XI3AqgJIOVi', 'ciccio', 'admin', '2024-05-13 18:28:06', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);


ALTER TABLE `recipes_category`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `recipes_recipes`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `recipes_user`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `recipes_ingredients`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `recipes_shopping_list`
  ADD PRIMARY KEY (`id`);


ALTER TABLE `recipes_category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;


ALTER TABLE `recipes_recipes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;


ALTER TABLE `recipes_user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
  
ALTER TABLE `recipes_ingredients`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

ALTER TABLE `recipes_shopping_list`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

