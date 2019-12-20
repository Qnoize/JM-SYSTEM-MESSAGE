package jm.model.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jm.model.Bot;
import jm.model.Channel;
import jm.model.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "messages")
@Inheritance(strategy = InheritanceType.JOINED)
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.TABLE)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bot_id")
    private Bot bot;

    @Column(name = "content", nullable = false)
    @EqualsAndHashCode.Include
    private String content;

    @Column(name = "date_create", nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Type(type = "org.hibernate.type.LocalDateTimeType")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime dateCreate;

    @Column(name = "filename")
    private String filename;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    // from ChannelMessage
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    // from ChannelMessage
    @Column(name = "shared_message_id")
    private Long sharedMessageId;

//    @ManyToMany(cascade = CascadeType.REFRESH)
//    @JoinTable(
//            name = "starred_message_user",
//            joinColumns = @JoinColumn(name = "msg_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//    private Set<User> starredByWhom;

    // from DirectMessage
    @ManyToMany
    @JoinTable(name = "messages_recipient_users",
            joinColumns = @JoinColumn(name = "direct_message_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_user_id", referencedColumnName = "id"))
    private Set<User> recipientUsers;

    // from ThreadChannelMessage and ThreadDirectMessage
    @ManyToOne
    private Message parentMessage;

    // ===================================
    // Construct
    // ===================================
    public Message(Channel channel, User user, String content, LocalDateTime dateCreate) {
        this.channel = channel;
        this.user = user;
        this.content = content;
        this.dateCreate = dateCreate;
    }

    public Message(Channel channel, Bot bot, String content, LocalDateTime dateCreate) {
        this.channel = channel;
        this.bot = bot;
        this.content = content;
        this.dateCreate = dateCreate;
    }

    public Message(Long id, Channel channel, User user, String content, LocalDateTime dateCreate) {
        this.id = id;
        this.channel = channel;
        this.user = user;
        this.content = content;
        this.dateCreate = dateCreate;
    }

    //two constructors for sharing messages
    public Message(Channel channel, User user, String content, LocalDateTime dateCreate, Long sharedMessageId) {
        this.channel = channel;
        this.user = user;
        this.content = content;
        this.dateCreate = dateCreate;
        this.sharedMessageId = sharedMessageId;
    }

    public Message(Channel channel, Bot bot, String content, LocalDateTime dateCreate, Long sharedMessageId) {
        this.channel = channel;
        this.bot = bot;
        this.content = content;
        this.dateCreate = dateCreate;
        this.sharedMessageId = sharedMessageId;
    }

}
