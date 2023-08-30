SELECT c FROM Comments c LEFT JOIN FETCH c.children WHERE c.parent IS NULL ORDER BY c.commentNo DESC

SELECT c FROM Comments c WHERE c.parent IS NULL AND EXISTS (SELECT child FROM Comments child WHERE child.parent = c) ORDER BY c.comment_No DESC

SELECT parent.commentNo AS parent_comment_No,
       parent.writer AS parent_writer,
       parent.content AS parent_content,
       parent.createdDate AS parent_createdDate,
       child.commentNo AS child_comment_No,
       child.writer AS child_writer,
       child.content AS child_content,
       child.createdDate AS child_createdDate
FROM Comments parent
LEFT JOIN Comments child ON parent.commentNo = child.parent_comment_No
WHERE parent.parent_comment_No IS NULL
ORDER BY parent.comment_No DESC, child.comment_No;

select * from BLAH.FKQ2QL0N61D4MLUJWWDJ4886X94

select * from posts

select * from users

delete from users where userId = 

select * from authority

select * from Comments

update AUTHORITY set role = 'ROLE_USER' where AUTHORITY_no = 65

delete from posts where post_no = 405

SELECT * 
FROM comments
WHERE post.post_no = 382

ALTER TABLE posts
RENAME COLUMN id TO user_no;

drop table replys

ALTER TABLE users
DROP COLUMN user_no;

ALTER TABLE AUTHORITY RENAME COLUMN ID TO AUTHORITY_NO;

drop table AUTHORITY
delete from posts
commit

ALTER TABLE Users
ADD (USER_NO NUMBER DEFAULT jpa_users_seq_generator.NEXTVAL);

ALTER TABLE Users
ADD USER_NO NUMBER DEFAULT jpa_users_seq.NEXTVAL;


CREATE SEQUENCE jpa_users_seq
  START WITH 1
  INCREMENT BY 1;
ALTER TABLE USERS
DROP COLUMN ID;

SELECT table_name, column_name
FROM all_cons_columns
WHERE constraint_name = 'BLAH.FKQ2QL0N61D4MLUJWWDJ4886X94';

delete from post_images

SELECT
    constraint_name,
    table_name,
    r_constraint_name,
    delete_rule
FROM
    all_constraints
WHERE
    constraint_name = 'FKQ2QL0N61D4MLUJWWDJ4886X94';

