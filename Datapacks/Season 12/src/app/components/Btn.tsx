import { Octicon } from './index.js'

type BtnProps = {
	icon?: keyof typeof Octicon,
	label?: string,
	active?: boolean,
	tooltip?: string,
	tooltipLoc?: 'se' | 'sw' | 'nw',
	class?: string,
	onClick?: (event: MouseEvent) => unknown,
	disabled?: boolean,
}
export function Btn({ icon, label, active, class: clazz, tooltip, tooltipLoc, onClick, disabled }: BtnProps) {
	return <div class={`btn${active ? ' active' : ''}${clazz ? ` ${clazz}` : ''}${tooltip ? ` tooltipped tip-${tooltipLoc ?? 'sw'}` : ''}${disabled ? ' disabled' : ''}${active ? ' tip-shown' : ''}`} onClick={disabled ? undefined : onClick} aria-label={tooltip}>
		{icon && Octicon[icon]}
		{label && <span>{label}</span>}
	</div>
}
