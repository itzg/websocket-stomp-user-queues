package me.itzg.wsuq.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.simp.user.UserDestinationResult;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Geoff Bourne
 * @since Feb 2017
 */
public class ConsistentUserDestinationResolver implements UserDestinationResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsistentUserDestinationResolver.class);


    private static final Pattern USER_DEST_PREFIXING_PATTERN =
            Pattern.compile("/user/(?<name>.+?)/(?<routing>.+)/(?<dest>.+?)");

    private static final Pattern USER_AUTHENTICATED_PATTERN =
            Pattern.compile("/user/(?<routing>.*)/(?<dest>.*?)");

    @Override
    public UserDestinationResult resolveDestination(Message<?> message) {
        SimpMessageHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, SimpMessageHeaderAccessor.class);

        final String destination = accessor.getDestination();
        final String authUser = accessor.getUser() != null ? accessor.getUser().getName() : null;
        LOGGER.debug("Resolving user destination {} for authUser={}, messageType={}",
                destination, authUser, accessor.getMessageType());

        if (destination != null) {
            if (accessor.getMessageType().equals(SimpMessageType.MESSAGE)) {
                final Matcher prefixMatcher = USER_DEST_PREFIXING_PATTERN.matcher(destination);
                if (prefixMatcher.matches()) {
                    String user = prefixMatcher.group("name");
                    String result = String.format("/%s/users.%s.%s",
                            prefixMatcher.group("routing"), user, prefixMatcher.group("dest"));
                    UserDestinationResult userDestinationResult =
                            new UserDestinationResult(destination, Collections.singleton(result), result, user);
                    LOGGER.debug("Resolved into {}", userDestinationResult);
                    return userDestinationResult;
                }
            }

            if (authUser != null) {
                final Matcher authMatcher = USER_AUTHENTICATED_PATTERN.matcher(destination);
                if (authMatcher.matches()) {
                    String result = String.format("/%s/users.%s.%s",
                            authMatcher.group("routing"), authUser, authMatcher.group("dest"));
                    UserDestinationResult userDestinationResult =
                            new UserDestinationResult(destination, Collections.singleton(result), result, authUser);
                    LOGGER.debug("Resolved into {}", userDestinationResult);
                    return userDestinationResult;
                }
            }
        }

        return null;
    }
}
