package xyz.n7mn.dev.chat;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.n7mn.dev.data.VoteReactionList;
import xyz.n7mn.dev.game.MoneySystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RoleCommand extends CommandClassInterface {

    /*
    JDA -> getJDA();
    Guild -> getGuild();
    TextChannel -> getTextChannel();
    Member -> getMember();
    User -> getUser();
    Message -> getMessage();
    String -> getMessageText();
    */

    public RoleCommand(TextChannel textChannel, Message message) {
        super(textChannel, message);
    }

    @Override
    public void run() {

        String[] split = getMessageText().split(" ", -1);

        Member member = null;
        if (split.length == 2 || split.length == 3){
            try {
                member = getGuild().getMemberById(split[1]);

                if (member == null){
                    for (Member m : getGuild().getMembers()){
                        if (m.getNickname() != null){
                            if (m.getNickname().contains(split[1])){
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
                        if (m.getNickname().contains(split[1])){
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

            StringBuffer sb = new StringBuffer();

            sb.append("----- ");
            if (member.getNickname() != null){
                sb.append(member.getNickname());
                sb.append(" (");
                sb.append(member.getUser().getAsTag());
                sb.append(")さんの情報 -----\n");
            } else {
                sb.append(member.getUser().getAsTag());
                sb.append("さんの情報 -----\n");
            }

            Date joinTime = Date.from(member.getTimeJoined().toInstant());
            boolean isBot = member.getUser().isBot();
            List<Role> roles = member.getRoles();

            sb.append("botかどうか : `");
            sb.append(isBot);
            sb.append("`\n");
            sb.append("入室日時 : `");
            sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(joinTime));
            sb.append("\n`");
            sb.append("ロール：\n");
            for (Role role : roles){
                sb.append("`");
                sb.append(role.getName());
                sb.append("`\n");
            }

            getMessage().reply(sb.toString()).queue(message1 -> {
                message1.addReaction("✅").queue();
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

            Role setRole;
            try {
                setRole = getGuild().getRoleById(split[2]);
            } catch (Exception e){
                setRole = null;
            }

            if (setRole == null){
                List<Role> roles = getGuild().getRoles();

                for (Role role : roles){

                    if (role.getName().equals(split[2])){
                        setRole = role;
                        break;
                    }

                }
            }

            if (setRole != null){

                boolean isRole = false;

                List<Role> roles = member.getRoles();
                for (Role role : roles){
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

                    getGuild().removeRoleFromMember(member, setRole).queue();
                    getMessage().reply(member.getUser().getName() + "さんからロール「"+setRole.getName()+"」を削除しました！").queue();
                } catch (Exception e){

                    getMessage().reply("ロール「"+setRole.getName()+"」は存在しないか 追加/削除することができないロールです！").queue();

                }

            }

        }

    }
}
