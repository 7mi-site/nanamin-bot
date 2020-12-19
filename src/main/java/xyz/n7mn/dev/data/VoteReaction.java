package xyz.n7mn.dev.data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.UUID;

public class VoteReaction {

    private UUID uuid;
    private Guild guild;
    private TextChannel channel;
    private String messageId;
    private Member member;
    private MessageReaction.ReactionEmote reactionEmote;

    public VoteReaction(UUID uuid, Guild guild, TextChannel channel, Member member, String messageId, MessageReaction.ReactionEmote reactionEmote){

        this.uuid = uuid;
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.reactionEmote = reactionEmote;

    }

    public VoteReaction(Guild guild, TextChannel channel, Member member, String messageId, MessageReaction.ReactionEmote reactionEmote) {

        this.uuid = UUID.randomUUID();
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.reactionEmote = reactionEmote;

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public void setChannel(TextChannel channel) {
        this.channel = channel;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageReaction.ReactionEmote getReactionEmote() {
        return reactionEmote;
    }

    public void setReactionEmote(MessageReaction.ReactionEmote reactionEmote) {
        this.reactionEmote = reactionEmote;
    }
}
