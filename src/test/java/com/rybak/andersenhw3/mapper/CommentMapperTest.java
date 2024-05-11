package com.rybak.andersenhw3.mapper;

import com.rybak.andersenhw3.dto.CommentCreateDto;
import com.rybak.andersenhw3.dto.CommentResponseDto;
import com.rybak.andersenhw3.dto.UserResponseDto;
import com.rybak.andersenhw3.entity.Comment;
import com.rybak.andersenhw3.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CommentMapper commentMapper;

    @Test
    void toComment_ShouldReturnComment() {
        String message = "message";
        String userId = "userId";
        CommentCreateDto commentCreateDto = new CommentCreateDto(message, userId);
        Comment expected = new Comment(null, message, null, null);

        Comment actual = commentMapper.toComment(commentCreateDto);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toCommentResponseDto_ShouldReturnCommentResponseDto() {
        String message = "message";
        UUID commentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        CommentResponseDto expected = new CommentResponseDto(commentId, message, responseDto);
        Comment comment = new Comment(commentId, message, user, null);
        Mockito.when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        CommentResponseDto actual = commentMapper.toCommentResponseDto(comment);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void toCommentResponseDtoList_ShouldReturnCommentResponseDtoList() {
        String message = "message";
        UUID commentId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        CommentResponseDto commentResponseDto = new CommentResponseDto(commentId, message, responseDto);
        List<CommentResponseDto> expected = List.of(commentResponseDto);
        Comment comment = new Comment(commentId, message, user, null);
        List<Comment> comments = List.of(comment);
        Mockito.when(userMapper.toUserResponseDto(user)).thenReturn(responseDto);

        List<CommentResponseDto> actual = commentMapper.toCommentResponseDtoList(comments);

        Assertions.assertEquals(expected, actual);
    }

}