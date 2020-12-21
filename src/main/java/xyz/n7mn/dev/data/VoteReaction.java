package xyz.n7mn.dev.data;

import net.dv8tion.jda.api.entities.*;

import java.util.UUID;

public class VoteReaction {

    private UUID uuid;
    private Guild guild;
    private TextChannel channel;
    private String messageId;
    private Member member;
    private MessageReaction.ReactionEmote reactionEmote;
    private Emote emote;

    @Deprecated
    public VoteReaction(UUID uuid, Guild guild, TextChannel channel, Member member, String messageId, MessageReaction.ReactionEmote reactionEmote){

        this.uuid = uuid;
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.reactionEmote = reactionEmote;

    }

    @Deprecated
    public VoteReaction(Guild guild, TextChannel channel, Member member, String messageId, MessageReaction.ReactionEmote reactionEmote) {

        this.uuid = UUID.randomUUID();
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.reactionEmote = reactionEmote;

    }

    public VoteReaction(UUID uuid, Guild guild, TextChannel channel, Member member, String messageId, Emote emote){

        this.uuid = uuid;
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.emote = emote;

    }

    public VoteReaction(Guild guild, TextChannel channel, Member member, String messageId, Emote emote){

        this.uuid = UUID.randomUUID();
        this.guild = guild;
        this.channel = channel;
        this.member = member;
        this.messageId = messageId;
        this.emote = emote;

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

    @Deprecated
    public MessageReaction.ReactionEmote getReactionEmote() {
        return reactionEmote;
    }

    @Deprecated
    public void setReactionEmote(MessageReaction.ReactionEmote reactionEmote) {
        this.reactionEmote = reactionEmote;
    }

    public Emote getEmote() {
        return emote;
    }

    public void setEmote(Emote emote) {
        this.emote = emote;
    }
}
