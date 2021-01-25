package xyz.n7mn.dev.Command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import xyz.n7mn.dev.i.Chat;
import xyz.n7mn.dev.i.HelpData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Role extends Chat {
    public Role(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public HelpData getHelpMessage() {
        return null;
    }

    @Override
    public void run() {
        String[] split = getMessageText().split(" ", -1);

        if (split[0].toLowerCase().equals("n.ui")){
            split = new String[]{"n.role",getUser().getId()};
        }

        Member member = null;
        if (split.length == 2 || split.length == 3){
            try {
                member = getGuild().getMemberById(split[1]);

                if (member == null){
                    for (Member m : getGuild().getMembers()){
                        if (m.getNickname() != null){
                            if (m.getNickname().startsWith(split[1])){
                                member = m;
                                break;
                            }
                        } else {
                            if (m.getUser().getAsTag().startsWith(split[1])){
                                member = m;
                                break;
                            }
                        }
                    }
                }

            } catch (Exception e){
                for (Member m : getGuild().getMembers()){
                    if (m.getNickname() != null){
                        if (m.getNickname().startsWith(split[1])){
                            member = m;
                            break;
                        }
                    } else {
                        if (m.getUser().getAsTag().startsWith(split[1])){
                            member = m;
                            break;
                        }
                    }
                }
            }

            if (member == null){
                getMessage().reply("このDiscord鯖には存在しないユーザーらしいですよ？").queue();
                return;
            }
        }

        if (split.length == 2){
            // 確認

            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(member.getColor());
            builder.setThumbnail(member.getUser().getAvatarUrl());

            String name = member.getNickname();
            if (name == null){
                name = member.getUser().getName();
            }

            builder.setTitle(member.getUser().getAsTag()+"さんの情報");
            builder.addField("表示名", name, false);
            builder.addField("ユーザーID", member.getId(), false);
            if (member.hasTimeJoined()){
                builder.addField("サーバー入室日", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(member.getTimeJoined().toInstant())), false);
            } else {
                builder.addField("サーバー入室日", "不明", false);
            }
            if (member.getTimeBoosted() != null){
                builder.addField("サーバーブースト日", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(member.getTimeBoosted().toInstant())), false);
            }
            List<net.dv8tion.jda.api.entities.Role> roleList = member.getRoles();

            StringBuffer buffer = new StringBuffer("以下の "+roleList.size()+"個 ついています。\n\n");
            for (net.dv8tion.jda.api.entities.Role role : roleList){
                buffer.append("```");
                buffer.append(role.getName());
                buffer.append("```");
            }

            builder.addField("ロール", buffer.toString(), false);



            getMessage().reply(builder.build()).queue(message -> {
                message.addReaction("\u2705").queue();
                message.addReaction("\u274C").queue();
            });
        }

        if (split.length == 3){
            // 追加
            Member na = getGuild().getMemberById("781323086624456735");
            if (na != null && !na.hasPermission(Permission.MANAGE_ROLES)){
                getMessage().reply("この鯖ではロール追加機能は使わせてもらえないです；；").queue();
                return;
            }

            Member id = getGuild().getMemberById(getMember().getId());
            if (id != null && !id.hasPermission(Permission.MANAGE_ROLES)){
                getMessage().reply("あなたはロール追加ができないみたいです。").queue();
                return;
            }

            net.dv8tion.jda.api.entities.Role setRole;
            try {
                setRole = getGuild().getRoleById(split[2]);
            } catch (Exception e){
                setRole = null;
            }

            if (setRole == null){
                List<net.dv8tion.jda.api.entities.Role> roles = getGuild().getRoles();

                for (net.dv8tion.jda.api.entities.Role role : roles){

                    if (role.getName().equals(split[2])){
                        setRole = role;
                        break;
                    }

                }
            }

            if (setRole != null){

                boolean isRole = false;

                List<net.dv8tion.jda.api.entities.Role> roles = member.getRoles();
                for (net.dv8tion.jda.api.entities.Role role : roles){
                    if (setRole.getId().equals(role.getId())){
                        isRole = true;
                        break;
                    }
                }

                try {
                    if (!isRole){
                        getGuild().addRoleToMember(member, setRole).queue();
                        getMessage().reply(member.getUser().getName() + "さんをロール「"+setRole.getName()+"」に追加をしました！").queue();
                        return;
                    }

                    Member bot = getGuild().getMemberById(getGuild().getJDA().getSelfUser().getId());
                    List<net.dv8tion.jda.api.entities.Role> roleList = bot.getRoles();
                    if (!roleList.get(0).canInteract(setRole)){
                        getMessage().reply("ロール「"+setRole.getName()+"」は追加/削除することができないロールです！").queue();
                        return;
                    }

                    getGuild().removeRoleFromMember(member, setRole).queue();
                    getMessage().reply(member.getUser().getName() + "さんからロール「"+setRole.getName()+"」を削除しました！").queue();
                } catch (Exception e){
                    getMessage().reply("ロール「"+setRole.getName()+"」は存在しないロールです！").queue();
                }

            }

        }

    }
}
