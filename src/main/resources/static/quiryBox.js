const code = `registerAnimator('demo', class {
  animate(currentTime, effect) {
    effect.localTime = currentTime;
  }
});`

const blob = new Blob([code], { type: 'text/javascript' })
const workletUrl = URL.createObjectURL(blob)

function init(aniWorklet) {
    aniWorklet.addModule(workletUrl);
    const topbarElement = document.querySelector('.J-topbar');
    const avatarElement = document.querySelector('.J-header-avatar');
    const nameElement = document.querySelector('.J-txt-shadow');
    const nameShadowElement = document.querySelector('.m-txt-shadow');
    const keyframeOptions = {
        duration: 1000,
        fill: 'both',
    };
    const avatarEffects = new KeyframeEffect(
        avatarElement,
        [
            {transform: 'translateY(0) scale(0.2)', offset: 0.5},
            {transform: 'translateY(0) scale(0)', },
        ],
        keyframeOptions
    );
    const barEffects = new KeyframeEffect(
        topbarElement,
        [
            {opacity: 0, offset: 0},
            {opacity: 1, offset: 1},
        ],
        keyframeOptions
    );
    const nameEffects = new KeyframeEffect(
        nameElement,
        [
            {},
            {},
            {}
        ],
        keyframeOptions
    );
    const nameShadowEffects = new KeyframeEffect(
        nameShadowElement,
        [
            {opacity: 0, offset: 0},
            {opacity: 1, offset: 0.5},
            {opacity: 1, offset: 1},
        ],
        keyframeOptions
    );

    const scrollSource = document.body;
    const timeline = new ScrollTimeline({
        scrollSource,
        timeRange: 1000
    });

    const animatorName = 'demo';

    [avatarEffects, barEffects, nameEffects, nameShadowEffects].forEach((item) => {
        new WorkletAnimation(
            animatorName,
            item, // 这里理论上是可以传一个effectList的，但是现在浏览器还不支持
            timeline,
        ).play();
    });
    function updateTimings() {
        const timeRange = 1000;
        const viewportHeight = scrollSource.clientHeight;
        const viewportWidth = scrollSource.clientWidth;

        // 可滚动的最大距离
        const maxScroll = scrollSource.scrollHeight - viewportHeight;
        if (maxScroll) {
            // 头像要缩小的系数
            const avatarScale = 0.4;
            // 初始头像中心到顶部的距离
            const avatarToTop = avatarElement.offsetTop + avatarElement.offsetHeight / 2 - 25;
            const offsetRange = avatarToTop / maxScroll;
            if (offsetRange < 1) { // 大于1的时候意味着头像未触碰顶部就已经滑到底了
                const avatarEffectKeyFrames = avatarEffects.getKeyframes();
                // 头像的最大位移
                const maxAvatarOffset = [
                    -viewportWidth / 2 + 60,
                    maxScroll - avatarToTop,
                ];
                avatarEffectKeyFrames[0].transform = `translate(${maxAvatarOffset[0]}px, 0) scale(${avatarScale})`;
                avatarEffectKeyFrames[0].offset = offsetRange;
                avatarEffectKeyFrames[1].transform = `translate(${maxAvatarOffset[0]}px, ${maxAvatarOffset[1]}px) scale(${avatarScale})`;
                avatarEffects.setKeyframes(avatarEffectKeyFrames);

                const barEffectKeyFrames = barEffects.getKeyframes();
                barEffectKeyFrames[0].opacity = 1;
                barEffectKeyFrames[0].offset = offsetRange;
                barEffects.setKeyframes(barEffectKeyFrames);

                const nameDistanceFromTop = avatarToTop + 60;
                const maxNameOffset = [
                    -viewportWidth / 2 + avatarElement.offsetWidth * 1.5 + 25,
                    maxScroll - nameDistanceFromTop
                ];
                const nameScale = 0.7;
                const nameEffectKeyFrames = nameEffects.getKeyframes();
                nameEffectKeyFrames[0].transform = `translate(0, 0) scale(1)`;
                nameEffectKeyFrames[0].offset = nameDistanceFromTop / maxScroll / 3;
                nameEffectKeyFrames[1].transform = `translate(${maxNameOffset[0]}px, 0px) scale(${nameScale}`;
                nameEffectKeyFrames[1].offset = nameDistanceFromTop / maxScroll;
                nameEffectKeyFrames[2].transform = `translate(${maxNameOffset[0]}px, ${maxNameOffset[1]}px) scale(${nameScale}`;
                nameEffectKeyFrames[2].offset = 1;
                nameEffects.setKeyframes(nameEffectKeyFrames);

                const nameShadowEffectKeyFrames = nameShadowEffects.getKeyframes();
                nameShadowEffectKeyFrames[1].offset = nameDistanceFromTop / maxScroll;
                nameShadowEffects.setKeyframes(nameShadowEffectKeyFrames);
            }
        }
    }
    updateTimings();
    window.addEventListener('resize', _ => updateTimings());
};
if (CSS.animationWorklet || window.animationWorklet) {
    init(CSS.animationWorklet || window.animationWorklet);
} else {
    console.error('请打开 chrome://flags/ --> “Experimental Web Platform features” 置为 Enabled')
}
