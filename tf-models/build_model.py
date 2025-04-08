# build_model.py
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

with tf.Graph().as_default():
    input_tensor = tf.placeholder(dtype=tf.string, shape=[], name='input_image')
    image = tf.image.decode_jpeg(input_tensor, channels=3)
    shape = tf.shape(image, name='output_shape')

    with tf.Session() as sess:
        tf.train.write_graph(sess.graph_def, '.', 'image_shape_model.pb', as_text=False)

